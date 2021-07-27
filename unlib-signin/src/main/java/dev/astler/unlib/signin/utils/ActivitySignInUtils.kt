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
import dev.astler.unlib.signin.interfaces.SignInActivityListener
import dev.astler.unlib.signin.ui.activity.contracts.SignInActivityContract
import dev.astler.unlib.utils.infoLog
import dev.astler.unlib.utils.makeToast
import dev.astler.unlib.utils.simpleTry

private var mFirebaseAuth: FirebaseAuth? = null
var mSignInGoogleLauncher: ActivityResultLauncher<String>? = null

fun getFirebaseAuth(): FirebaseAuth {
    return if (mFirebaseAuth == null) {
        val nAuth = Firebase.auth
        mFirebaseAuth = nAuth
        nAuth
    } else mFirebaseAuth as FirebaseAuth
}

fun AppCompatActivity.getFirebaseUser(): FirebaseUser? {
    return getFirebaseAuth().currentUser
}

fun AppCompatActivity.getFirebaseUserId(): String {
    return getFirebaseAuth().currentUser?.uid ?: ""
}

fun AppCompatActivity.createSingInWithGoogleLauncher() = registerForActivityResult(SignInActivityContract()) { pTask ->
    infoLog("task null?:")

    if (pTask == null) return@registerForActivityResult

    simpleTry {
        infoLog("return?:")
        val account = pTask.getResult(ApiException::class.java)!!
        infoLog("firebaseAuthWithGoogle:" + account.id)
        this.authWithGoogle(account.idToken)
    }
}

fun AppCompatActivity.signInWithGoogle(pInput: String = "sign_in") {
    mSignInGoogleLauncher?.launch(pInput)
}

private fun AppCompatActivity.authWithGoogle(idToken: String? = null) {
    val credential = GoogleAuthProvider.getCredential(idToken, null)

    infoLog("try with google!")

    getFirebaseAuth().signInWithCredential(credential)
        .addOnCompleteListener(this) { task ->

            infoLog("so? google!")

            val nUser = if (task.isSuccessful) {
                infoLog("signInWithCredential:success")
                getFirebaseAuth().currentUser
            } else {
                infoLog("signInWithCredential:failure ${task.exception}")
                null
            }

            if (this is SignInActivityListener)
                updateUI(nUser)
        }
}

fun AppCompatActivity.createUserWithEmailAndPassword(pEmail: String? = null, pPassword: String? = null) {
    if (pEmail == null || pPassword == null) {
        makeToast(R.string.registration_error)
        return
    }

    if (pEmail.isEmpty() || pPassword.isEmpty()) {
        return
    }

    getFirebaseAuth().createUserWithEmailAndPassword(pEmail, pPassword)
        ?.addOnCompleteListener(this) { task ->
            val nUser = if (task.isSuccessful) {
                infoLog("signInWithCredential:success")
                getFirebaseAuth().currentUser
            } else {
                infoLog("signInWithCredential:failure ${task.exception}")
                null
            }

            if (this is SignInActivityListener)
                updateUI(nUser)
        }
}

fun AppCompatActivity.authWithEmailAndPassword(pEmail: String? = null, pPassword: String? = null) {
    if (pEmail == null || pPassword == null) {
        makeToast(R.string.sign_in_error)
        return
    }

    if (pEmail.isEmpty() || pPassword.isEmpty()) {
        return
    }

    getFirebaseAuth().signInWithEmailAndPassword(pEmail, pPassword)
        .addOnCompleteListener(this) { task ->
            val nUser = if (task.isSuccessful) {
                infoLog("signInWithCredential:success")
                getFirebaseAuth().currentUser
            } else {
                infoLog("signInWithCredential:failure ${task.exception}")
                null
            }

            if (this is SignInActivityListener)
                updateUI(nUser)
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

    infoLog("TOKEN code = ${getString(R.string.default_web_client_id)}")

    return GoogleSignIn.getClient(this, gso)
}

/**
 * Activity Functions
 */

fun AppCompatActivity.signInInitializer() {
    mSignInGoogleLauncher = createSingInWithGoogleLauncher()
}

fun AppCompatActivity.signInOnResume() {
    val currentUser = getFirebaseUser()

    if (this is SignInActivityListener)
        updateUI(currentUser)

//    if (currentUser == null)
//        signIn()
}
