package dev.astler.catlib.signin.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

interface IFirebaseAuthRepository {
    val user: FirebaseUser? get
    val userId: String? get
    val isSignedIn: Boolean get() = user != null
    val auth: FirebaseAuth get
    fun signOut()
}