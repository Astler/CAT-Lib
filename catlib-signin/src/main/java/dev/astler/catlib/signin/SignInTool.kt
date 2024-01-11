package dev.astler.catlib.signin

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dev.astler.catlib.config.AppConfig
import dev.astler.catlib.helpers.errorLog
import dev.astler.catlib.helpers.infoLog
import dev.astler.catlib.helpers.trackedTry
import dev.astler.catlib.preferences.PreferencesTool
import dev.astler.catlib.remote_config.RemoteConfigProvider
import dev.astler.catlib.signin.interfaces.ISignInListener
import dev.astler.catlib.signin.utils.startOptionalSignIn
import javax.inject.Inject

class SignInTool @Inject constructor(
    private val _context: Context,
    val preferences: PreferencesTool,
    private val remoteConfig: RemoteConfigProvider,
    val appConfig: AppConfig
) {
    private var _signInListener: ISignInListener? = null

    private val _auth by lazy {
        Firebase.auth
    }

    private val _oneTapClient: SignInClient by lazy {
        Identity.getSignInClient(_context)
    }


    private val _signInRequest: BeginSignInRequest by lazy {
        BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            )
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(_context.getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    private val _signInLauncher by lazy {
        if (_context !is AppCompatActivity) {
            errorLog("AdsTool: Context is not AppCompatActivity")
            return@lazy null
        }

        _context.registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult

            val data: Intent? = result.data

            trackedTry {
                val googleCredential = _oneTapClient.getSignInCredentialFromIntent(data)
                val idToken = googleCredential.googleIdToken

                when {
                    idToken != null -> {
                        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                        _auth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener(_context) { task ->
                                if (task.isSuccessful) {
                                    infoLog("signInWithCredential:success")
                                    val user = _auth.currentUser
                                    _signInListener?.updateUI(user)
                                } else {
                                    errorLog("signInWithCredential:failure: ${task.exception}")
                                    _signInListener?.updateUI(null)
                                }
                            }
                    }

                    else -> {
                        errorLog("No ID token!")
                    }
                }
            }
        }
    }

    init {
        if (_context !is AppCompatActivity) {
            errorLog("AdsTool: Context is not AppCompatActivity")
        }

        if (_context is ISignInListener) {
            _signInListener = _context
        }
    }

    fun universalSignInRequest() {
        if (_auth.currentUser != null) return

        fun tryToSignInWithPicker() {
            _context.startOptionalSignIn()
        }

        infoLog("universalSignInRequest start")
        _signInRequest.let {
            infoLog("_signInRequest $_signInRequest")
            _oneTapClient.beginSignIn(it)
                .addOnSuccessListener { result ->
                    infoLog("_oneTapClient result $result")
                    try {
                        val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                        infoLog("_signInLauncher $_signInLauncher")
                        _signInLauncher?.launch(intentSenderRequest)
                    } catch (e: Exception) {
                        infoLog("_oneTapClient catch ${e.message}")
                        errorLog(e)
                        tryToSignInWithPicker()
                    }
                }
                .addOnFailureListener { e ->
                    errorLog(e)
                    infoLog("_oneTapClient failed ${e.message}")
                    tryToSignInWithPicker()
                }
        }
    }
}