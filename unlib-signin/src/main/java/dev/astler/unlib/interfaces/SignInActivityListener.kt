package dev.astler.unlib.interfaces

import com.google.firebase.auth.FirebaseUser

interface SignInActivityListener {
    fun updateUI(pUser: FirebaseUser? = null)
}
