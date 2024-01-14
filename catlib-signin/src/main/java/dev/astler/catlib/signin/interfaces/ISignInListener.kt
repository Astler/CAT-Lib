package dev.astler.catlib.signin.interfaces

import com.google.firebase.auth.FirebaseUser

interface ISignInListener {
    fun onSignIn(user: FirebaseUser? = null) {}
    fun onSignOut() {}

    fun updateUI(user: FirebaseUser? = null)
}
