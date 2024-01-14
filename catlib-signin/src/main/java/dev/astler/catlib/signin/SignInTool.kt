package dev.astler.catlib.signin

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import dev.astler.catlib.config.AppConfig
import dev.astler.catlib.helpers.errorLog
import dev.astler.catlib.helpers.infoLog
import dev.astler.catlib.helpers.trackedTry
import dev.astler.catlib.preferences.PreferencesTool
import dev.astler.catlib.remote_config.RemoteConfigProvider
import dev.astler.catlib.signin.data.IFirebaseAuthRepository
import dev.astler.catlib.signin.data.SignInViewModel
import dev.astler.catlib.signin.interfaces.ISignInListener
import dev.astler.catlib.signin.utils.startOptionalSignIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignInTool @Inject constructor(
    private val _context: Context,
    val preferences: PreferencesTool,
    private val remoteConfig: RemoteConfigProvider,
    private val _firebaseAuthRepository: IFirebaseAuthRepository,
    val appConfig: AppConfig
) {
    private var _signInLauncher: ActivityResultLauncher<IntentSenderRequest>? = null
    private var _signInListener: ISignInListener? = null

    private val _oneTapClient: SignInClient by lazy {
        Identity.getSignInClient(_context)
    }

    val signInViewModel: SignInViewModel? by lazy {
        if (_context !is AppCompatActivity) {
            errorLog("AdsTool: Context is not AppCompatActivity")
            return@lazy null
        }

        ViewModelProvider(_context)[SignInViewModel::class.java]
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
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    init {
        if (_context is ISignInListener) {
            _signInListener = _context
        }

        initializeForActivity()
    }

    private fun initializeForActivity() {
        if (_context !is AppCompatActivity) {
            errorLog("AdsTool: Context is not AppCompatActivity")
            return
        }

        signInViewModel?.user?.observe(_context) {
            _signInListener?.updateUI(it)
        }

        _context.lifecycleScope.launch {
            _context.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                _signInLauncher = _context.registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                    if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult

                    val data: Intent? = result.data

                    trackedTry {
                        val googleCredential = _oneTapClient.getSignInCredentialFromIntent(data)
                        val idToken = googleCredential.googleIdToken

                        when {
                            idToken != null -> {
                                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                                _firebaseAuthRepository.auth.signInWithCredential(firebaseCredential)
                                    .addOnCompleteListener(_context) { task ->
                                        if (task.isSuccessful) {
                                            infoLog("signInWithCredential:success")
                                            val user = _firebaseAuthRepository.user
                                            _signInListener?.onSignIn(user)
                                        } else {
                                            errorLog("signInWithCredential:failure: ${task.exception}")
                                        }

                                        signInViewModel?.setupUserData(_firebaseAuthRepository.user)
                                    }
                            }

                            else -> {
                                errorLog("No ID token!")
                            }
                        }
                    }
                }
            }

            _context.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                signInViewModel?.setupUserData(_firebaseAuthRepository.user)
            }
        }
    }

    fun signOut() {
        _firebaseAuthRepository.signOut()
        _signInListener?.onSignOut()
        signInViewModel?.setupUserData(null)
    }

    fun universalSignInRequest() {
        if (_firebaseAuthRepository.isSignedIn) return

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