package dev.astler.unlib.signin.utils

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dev.astler.unlib.signin.R
import dev.astler.unlib.signin.activity.contracts.SignInActivityContract
import dev.astler.unlib.signin.interfaces.SignInActivityListener
import dev.astler.unlib.utils.infoLog
import dev.astler.unlib.utils.simpleTry

private val AppCompatActivity.mAuth: FirebaseAuth? by lazy {
    Firebase.auth
}

var mSignInGoogleLauncher: ActivityResultLauncher<String>? = null

fun AppCompatActivity.signInInitializer() {
    mSignInGoogleLauncher = createSingInWithGoogleLauncher()
}

fun AppCompatActivity.getFirebaseUser(): FirebaseUser? {
    return mAuth?.currentUser
}

fun AppCompatActivity.getFirebaseUserId(): String {
    return mAuth?.currentUser?.uid ?: ""
}

fun AppCompatActivity.createSingInWithGoogleLauncher() = registerForActivityResult(SignInActivityContract()) { pTask ->
    if (pTask == null) return@registerForActivityResult

    simpleTry {
        val account = pTask.getResult(ApiException::class.java)!!
        infoLog("firebaseAuthWithGoogle:" + account.id)

        if (this is SignInActivityListener)
            this.authWithGoogle(account.idToken)
    }
}

enum class SingInMode {
    GOOGLE
}

fun AppCompatActivity.signIn(pSignInMode: SingInMode = SingInMode.GOOGLE, pInput: String = "sign_in") {
    when (pSignInMode) {
        SingInMode.GOOGLE -> {
            mSignInGoogleLauncher?.launch(pInput)
        }
    }
}

private fun AppCompatActivity.authWithGoogle(idToken: String? = null) {
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    mAuth?.signInWithCredential(credential)
        ?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                infoLog("signInWithCredential:success")

                if (this is SignInActivityListener)
                    updateUI(mAuth?.currentUser)
            } else {
                infoLog("signInWithCredential:failure ${task.exception}")

                if (this is SignInActivityListener)
                    updateUI(null)
            }
        }
}

fun AppCompatActivity.signOut() {
    Firebase.auth.signOut()

    getGoogleSignInClient().signOut().addOnCompleteListener(this) {
        if (this is SignInActivityListener)
            updateUI(null)
    }
}

fun Context.getGoogleSignInClient(): GoogleSignInClient {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    return GoogleSignIn.getClient(this, gso)
}
