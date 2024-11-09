package dev.astler.catlib.signin

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dev.astler.catlib.config.AppConfig
import dev.astler.catlib.helpers.errorLog
import dev.astler.catlib.helpers.hasGoogleServices
import dev.astler.catlib.helpers.infoLog
import dev.astler.catlib.helpers.trackedTry
import dev.astler.catlib.signin.data.CatSignInMode
import dev.astler.catlib.signin.data.IFirebaseAuthRepository
import dev.astler.catlib.signin.data.SignInViewModel
import dev.astler.catlib.signin.interfaces.ISignInListener
import dev.astler.catlib.signin.ui.activity.contracts.EmailSignInActivityContract
import dev.astler.catlib.signin.ui.activity.contracts.SignInActivityContract
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignInManager @Inject constructor(
    private val context: Context,
    private val firebaseAuthRepository: IFirebaseAuthRepository,
    private val appConfig: AppConfig
) {
    // Region: Properties
    private var signInListener: ISignInListener? = null
    private var areSignInLaunchersRegistered = false

    private val signInViewModel: SignInViewModel? by lazy { initializeViewModel() }
    private val googleSignInProvider = GoogleSignInProvider()
    private val emailSignInProvider = EmailSignInProvider()
    private val credentialSignInProvider = CredentialSignInProvider()

    val isSignedIn: Boolean get() = firebaseAuthRepository.isSignedIn
    val user get() = firebaseAuthRepository.user
    val photoObservable get() = signInViewModel?.photoUrl ?: MutableLiveData(null)
    val signedInObservable get() = signInViewModel?.signedIn ?: MutableLiveData(false)

    init {
        setupInitialState()
    }

    // Region: Initialization
    private fun setupInitialState() {
        if (context is ISignInListener) {
            signInListener = context
        }
        infoLog("Initialization", "SignInManager")
        initializeForActivity()
    }

    private fun initializeViewModel(): SignInViewModel? {
        if (context !is AppCompatActivity) {
            errorLog("SignInManager: Context is not AppCompatActivity")
            return null
        }
        return ViewModelProvider(context)[SignInViewModel::class.java]
    }

    private fun initializeForActivity() {
        if (!isValidActivityContext()) return

        context as AppCompatActivity
        setupViewModelObservers()
        initializeLifecycleHandlers()
    }

    private fun setupViewModelObservers() {
        signInViewModel?.user?.observe(context as AppCompatActivity) {
            signInListener?.updateUI(it)
        }
        signInViewModel?.setupUserData(firebaseAuthRepository.user)
    }

    private fun initializeLifecycleHandlers() {
        (context as AppCompatActivity).lifecycleScope.launch {
            context.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                initializeProviders()
            }
            context.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                signInViewModel?.setupUserData(firebaseAuthRepository.user)
            }
        }
    }

    private fun initializeProviders() {
        if (areSignInLaunchersRegistered) return

        context as AppCompatActivity

        credentialSignInProvider.initialize(
            activity = context,
            appConfig = appConfig,
            onSuccess = { handleSignInSuccess(it) },
            onFailure = { tryGoogleDialogSignIn() }
        )

        googleSignInProvider.initialize(
            activity = context,
            onSuccess = { handleSignInSuccess(it) },
            onFailure = { signInWithEmail() }
        )

        emailSignInProvider.initialize(
            activity = context,
            onSuccess = { handleEmailSignInSuccess(it) }
        )

        areSignInLaunchersRegistered = true
    }

    fun trySignIn() {
        if (!canProceedWithSignIn()) return
        tryCredentialSignIn()
    }

    fun tryCredentialSignIn() {
        if (!canProceedWithSignIn()) return
        credentialSignInProvider.trySignIn(
            onFailure = { tryGoogleDialogSignIn() }
        )
    }

    fun tryGoogleDialogSignIn(signInMode: CatSignInMode = CatSignInMode.OPTIONAL) {
        if (!canProceedWithSignIn()) return
        if (!context.hasGoogleServices()) return

        googleSignInProvider.trySignIn(
            onFailure = { tryEmailSignIn(signInMode) }
        )
    }

    fun tryEmailSignIn(signInMode: CatSignInMode = CatSignInMode.OPTIONAL) {
        if (!canProceedWithSignIn()) return
        emailSignInProvider.trySignIn(signInMode)
    }

    fun signOut() {
        firebaseAuthRepository.signOut()
        signInListener?.onSignOut()
        googleSignInProvider.signOut(context)
        signInViewModel?.setupUserData(null)
    }

    fun createUserWithEmailAndPassword(
        email: String?,
        password: String?,
        callback: (FirebaseUser?, String?) -> Unit
    ) {
        if (!validateEmailAndPassword(email, password)) return

        firebaseAuthRepository.auth.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(context as AppCompatActivity) { task ->
                callback(
                    trackedTry(fallbackValue = null) { task.result.user },
                    task.exception?.message
                )
            }
    }

    fun authWithEmailAndPassword(
        email: String?,
        password: String?,
        callback: (FirebaseUser?, String?) -> Unit
    ) {
        if (!validateEmailAndPassword(email, password)) return

        firebaseAuthRepository.auth.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(context as AppCompatActivity) { task ->
                callback(
                    trackedTry(fallbackValue = null) { task.result.user },
                    task.exception?.message
                )
            }
    }

    // Region: Private Methods
    private fun signInWithEmail(signInMode: CatSignInMode = CatSignInMode.OPTIONAL) {
        emailSignInProvider.trySignIn(signInMode)
    }

    private fun handleSignInSuccess(idToken: String?) {
        if (idToken == null) {
            errorLog("No ID token!")
            return
        }

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuthRepository.auth.signInWithCredential(credential)
            .addOnCompleteListener(context as AppCompatActivity) { task ->
                if (task.isSuccessful) {
                    infoLog("signInWithCredential:success")
                    signInListener?.onSignIn(firebaseAuthRepository.user)
                } else {
                    errorLog("signInWithCredential:failure: ${task.exception}")
                }
                signInViewModel?.setupUserData(firebaseAuthRepository.user)
            }
    }

    private fun handleEmailSignInSuccess(user: FirebaseUser?) {
        val finalUser = user ?: firebaseAuthRepository.user
        signInListener?.onSignIn(finalUser)
        signInViewModel?.setupUserData(finalUser)
    }

    private fun isValidActivityContext(): Boolean {
        if (context !is AppCompatActivity) {
            errorLog("Context is not AppCompatActivity")
            return false
        }
        return true
    }

    private fun canProceedWithSignIn(): Boolean {
        if (firebaseAuthRepository.isSignedIn) return false
        if (!areSignInLaunchersRegistered) {
            errorLog("Sign in launchers not registered yet")
            return false
        }
        return isValidActivityContext()
    }

    private fun validateEmailAndPassword(email: String?, password: String?): Boolean {
        if (!isValidActivityContext()) return false
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) return false
        return true
    }

    private inner class GoogleSignInProvider {
        private var launcher: ActivityResultLauncher<String>? = null

        fun initialize(
            activity: AppCompatActivity,
            onSuccess: (String?) -> Unit,
            onFailure: () -> Unit
        ) {
            launcher = activity.registerForActivityResult(SignInActivityContract()) { task ->
                try {
                    val account = task?.getResult(ApiException::class.java)
                    if (account != null) {
                        onSuccess(account.idToken)
                    } else {
                        // No account but no exception means user cancelled
                        infoLog("User cancelled Google sign in")
                    }
                } catch (e: ApiException) {
                    when (e.statusCode) {
                        GoogleSignInStatusCodes.SIGN_IN_CANCELLED,
                        GoogleSignInStatusCodes.CANCELED -> {
                            // User cancelled, don't trigger failure
                            infoLog("User cancelled Google sign in")
                        }

                        else -> {
                            errorLog("Google sign in failed: ${e.message}")
                            onFailure()
                        }
                    }
                } catch (e: Exception) {
                    errorLog("Unexpected error during Google sign in: ${e.message}")
                    onFailure()
                }
            }
        }

        fun trySignIn(onFailure: () -> Unit = {}) {
            launcher?.launch("sign_in") ?: run {
                errorLog("Google sign in launcher not initialized")
                onFailure()
            }
        }

        fun signOut(context: Context) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            GoogleSignIn.getClient(context, gso).signOut()
        }
    }

    private inner class EmailSignInProvider {
        private var launcher: ActivityResultLauncher<Int>? = null

        fun initialize(
            activity: AppCompatActivity,
            onSuccess: (FirebaseUser?) -> Unit
        ) {
            launcher = activity.registerForActivityResult(EmailSignInActivityContract()) { user ->
                if (user != null) {
                    onSuccess(user)
                } else {
                    infoLog("User cancelled email sign in")
                }
            }
        }

        fun trySignIn(signInMode: CatSignInMode = CatSignInMode.OPTIONAL) {
            launcher?.launch(signInMode.ordinal) ?: run {
                errorLog("Email sign in launcher not initialized")
            }
        }
    }

    private inner class CredentialSignInProvider {
        private var credentialManager: CredentialManager? = null

        fun initialize(
            activity: AppCompatActivity,
            appConfig: AppConfig,
            onSuccess: (String?) -> Unit,
            onFailure: () -> Unit
        ) {
            credentialManager = CredentialManager.create(activity)
        }

        fun trySignIn(onFailure: () -> Unit = {}) {
            if (context !is AppCompatActivity || credentialManager == null) {
                errorLog("Invalid context or credential manager not initialized")
                onFailure()
                return
            }

            context.lifecycleScope.launch {
                try {
                    val result = credentialManager?.getCredential(
                        request = createGetCredentialRequest(),
                        context = context
                    )
                    handleCredentialResponse(result, onFailure)
                } catch (e: GetCredentialException) {
                    when {
                        e.isCancellation -> {
                            // User cancelled, don't trigger failure
                            infoLog("User cancelled credential sign in")
                        }

                        else -> {
                            errorLog("Credential Exception: ${e.message}")
                            onFailure()
                        }
                    }
                } catch (e: Exception) {
                    errorLog("General Exception: ${e.message}")
                    onFailure()
                }
            }
        }

        private val GetCredentialException.isCancellation: Boolean
            get() = message?.contains("canceled", ignoreCase = true) == true ||
                    message?.contains("cancelled", ignoreCase = true) == true

        private fun createGetCredentialRequest(): GetCredentialRequest {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(true)
                .setServerClientId(appConfig.signInClientId)
                .build()

            return GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()
        }

        private fun handleCredentialResponse(
            result: GetCredentialResponse?,
            onFailure: () -> Unit
        ) {
            if (result == null) {
                errorLog("Received null credential result")
                onFailure()
                return
            }

            when (val credential = result.credential) {
                is CustomCredential -> {
                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        try {
                            val googleIdTokenCredential = GoogleIdTokenCredential
                                .createFrom(credential.data)
                            handleSignInSuccess(googleIdTokenCredential.idToken)
                        } catch (e: GoogleIdTokenParsingException) {
                            errorLog(e, "Invalid google id token response")
                            onFailure()
                        }
                    } else {
                        errorLog("Unexpected credential type")
                        onFailure()
                    }
                }

                else -> {
                    errorLog("Unexpected credential type")
                    onFailure()
                }
            }
        }
    }
}