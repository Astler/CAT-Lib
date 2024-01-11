package dev.astler.catlib.signin.interfaces

import com.google.firebase.auth.FirebaseUser

interface ISignInListener {
    fun updateUI(pUser: FirebaseUser? = null)
}
