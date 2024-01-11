package dev.astler.catlib.signin.utils

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
import dev.astler.catlib.extensions.toast
import dev.astler.catlib.helpers.hasGoogleServices
import dev.astler.catlib.helpers.infoLog
import dev.astler.catlib.helpers.trackedTry
import dev.astler.catlib.signin.interfaces.ISignInListener
import dev.astler.catlib.signin.ui.activity.contracts.SignInActivityContract
import dev.astler.catlib.signin.R

private var mFirebaseAuth: FirebaseAuth? = null
var mSignInGoogleLauncher: ActivityResultLauncher<String>? = null

fun getFirebaseAuth(): FirebaseAuth {
    return if (mFirebaseAuth == null) {
        val nAuth = Firebase.auth
        mFirebaseAuth = nAuth
        nAuth
    } else mFirebaseAuth as FirebaseAuth
}

fun getFirebaseUser(): FirebaseUser? {
    return getFirebaseAuth().currentUser
}

fun getFirebaseUserId(): String {
    return getFirebaseAuth().currentUser?.uid ?: ""
}

fun AppCompatActivity.createSingInWithGoogleLauncher() = registerForActivityResult(SignInActivityContract()) { pTask ->
    infoLog("task null?:")

    if (pTask == null) return@registerForActivityResult

    trackedTry {
        val account = pTask.getResult(ApiException::class.java)!!
        infoLog("firebaseAuthWithGoogle:" + account.id)
        this.authWithGoogle(account.idToken)
    }
}

fun AppCompatActivity.signInWithGoogle(pInput: String = "sign_in") {
    if (hasGoogleServices())
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
                toast("Success!")
                getFirebaseAuth().currentUser
            } else {
                infoLog("signInWithCredential:failure ${task.exception}")
                toast(task.exception.toString())
                null
            }

            if (this is ISignInListener)
                updateUI(nUser)
        }
}

fun AppCompatActivity.createUserWithEmailAndPassword(pEmail: String? = null, pPassword: String? = null) {
    if (pEmail == null || pPassword == null) {
        toast(R.string.registration_error)
        return
    }

    if (pEmail.isEmpty() || pPassword.isEmpty()) {
        return
    }

    getFirebaseAuth().createUserWithEmailAndPassword(pEmail, pPassword)
        .addOnCompleteListener(this) { task ->
            val nUser = if (task.isSuccessful) {
                infoLog("createWithCredential:success")
                toast("Success!")
                getFirebaseAuth().currentUser
            } else {
                infoLog("createWithCredential:failure ${task.exception}")
                toast(task.exception.toString())
                null
            }

            if (this is ISignInListener)
                updateUI(nUser)
        }
}

fun AppCompatActivity.authWithEmailAndPassword(pEmail: String? = null, pPassword: String? = null) {
    if (pEmail == null || pPassword == null) {
        toast(R.string.sign_in_error)
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
                toast(R.string.sign_in_error)
                infoLog("signInWithCredential:failure ${task.exception}")
                null
            }

            if (this is ISignInListener)
                updateUI(nUser)
        }
}

fun AppCompatActivity.signOut() {
    Firebase.auth.signOut()

    getGoogleSignInClient().signOut().addOnCompleteListener(this) {
        if (this is ISignInListener)
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

    if (this is ISignInListener)
        updateUI(currentUser)

//    if (currentUser == null)
//        signIn()
}
