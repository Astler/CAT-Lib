package dev.astler.catlib.signin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dev.astler.catlib.config.AppConfig
import dev.astler.catlib.helpers.errorLog
import dev.astler.catlib.helpers.infoLog
import dev.astler.catlib.helpers.trackedTry
import dev.astler.catlib.preferences.PreferencesTool
import dev.astler.catlib.signin.data.IFirebaseAuthRepository
import dev.astler.catlib.signin.data.SignInViewModel
import dev.astler.catlib.signin.interfaces.ISignInListener
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignInTool @Inject constructor(
    private val _context: Context,
    val preferences: PreferencesTool,
    private val _appConfig: AppConfig,
    private val _firebaseAuthRepository: IFirebaseAuthRepository
) {
    private var _credentialManager: CredentialManager? = null
    private var _signInListener: ISignInListener? = null

    private val signInViewModel: SignInViewModel? by lazy {
        if (_context !is AppCompatActivity) {
            errorLog("SignInTool: Context is not AppCompatActivity")
            return@lazy null
        }

        ViewModelProvider(_context)[SignInViewModel::class.java]
    }

    val isSignedIn: Boolean
        get() = _firebaseAuthRepository.isSignedIn

    val photoObservable get() = signInViewModel?.photoUrl ?: MutableLiveData(null)
    val signedInObservable get() = signInViewModel?.signedIn ?: MutableLiveData(false)

    val user get() = _firebaseAuthRepository.user

    init {
        if (_context is ISignInListener) {
            _signInListener = _context
        }

        infoLog("Initialization", "SignInTool")
        initializeForActivity()
    }

    private fun handleSignIn(result: GetCredentialResponse?) {
        if (result == null) {
            errorLog("Received null result", postCategory = "SignInTool")
            return
        }

        infoLog("handleSignIn: ${result.credential}", "SignInTool")
        when (val credential = result.credential) {

            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        infoLog("Send: $googleIdTokenCredential", "SignInTool")
                        signInWithTokenId(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        errorLog(e, "Received an invalid google id token response", postCategory = "SignInTool")
                    }
                } else {
                    errorLog("Unexpected type of credential", postCategory = "SignInTool")
                }
            }

            else -> {
                errorLog("Unexpected type of credential", postCategory = "SignInTool")
            }
        }
    }

    private fun signInWithTokenId(idToken: String?) {
        if (_context !is AppCompatActivity) {
            errorLog("Context is not AppCompatActivity", postCategory = "SignInTool")
            return
        }

        when {
            idToken != null -> {
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                _firebaseAuthRepository.auth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(_context) { task ->
                        if (task.isSuccessful) {
                            infoLog("signInWithCredential:success", postCategory = "SignInTool")
                            val user = _firebaseAuthRepository.user
                            _signInListener?.onSignIn(user)
                        } else {
                            errorLog("signInWithCredential:failure: ${task.exception}", postCategory = "SignInTool")
                        }

                        signInViewModel?.setupUserData(_firebaseAuthRepository.user)
                    }
            }

            else -> {
                errorLog("No ID token!", postCategory = "SignInTool")
            }
        }
    }

    private fun initializeForActivity() {
        if (_context !is AppCompatActivity) {
            errorLog("Context is not AppCompatActivity", postCategory = "SignInTool")
            return
        }

        _credentialManager = CredentialManager.create(_context)

        signInViewModel?.user?.observe(_context) {
            _signInListener?.updateUI(it)
        }

        signInViewModel?.setupUserData(_firebaseAuthRepository.user)
    }

    fun processEmailSignIn(returnUser: FirebaseUser?): FirebaseUser? {
        val user = returnUser ?: _firebaseAuthRepository.user

        trackedTry {
            _signInListener?.onSignIn(user)
            signInViewModel?.setupUserData(user)
        }

        return user
    }

    fun signOut() {
        _firebaseAuthRepository.signOut()
        _signInListener?.onSignOut()
        signInViewModel?.setupUserData(null)
    }

    fun tryToSignIn() {
        if (_firebaseAuthRepository.isSignedIn) return
        if (_context !is AppCompatActivity) {
            errorLog("Context is not AppCompatActivity", postCategory = "SignInTool")
            return
        }

        val clientId = _appConfig.signInClientId
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(clientId)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        _context.lifecycleScope.launch {
            try {
                val result = _credentialManager?.getCredential(
                    request = request,
                    context = _context,
                )

                infoLog("Received result: $result", "SignInTool")
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                errorLog(e, postCategory = "SignInTool")
            }
        }
    }

    fun createUserWithEmailAndPassword(email: String?, password: String?, callback: (FirebaseUser?, String?) -> Unit) {
        if (_context !is AppCompatActivity) return
        if (email == null || password == null) return
        if (email.isEmpty() || password.isEmpty()) return

        _firebaseAuthRepository.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(_context) { task ->
                callback(trackedTry(fallbackValue = null) {
                    task.result.user
                }, task.exception?.message)
            }
    }

    fun authWithEmailAndPassword(email: String?, password: String?, callback: (FirebaseUser?, String?) -> Unit) {
        if (_context !is AppCompatActivity) return
        if (email == null || password == null) return
        if (email.isEmpty() || password.isEmpty()) return

        _firebaseAuthRepository.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(_context) { task ->
                callback(trackedTry(fallbackValue = null) {
                    task.result.user
                }, task.exception?.message)
            }
    }
}