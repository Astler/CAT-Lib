package dev.astler.catlib.signin.data

import android.content.Context
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(private val _context: Context) : IFirebaseAuthRepository {
    private val _firebaseAuth = Firebase.auth

    override val auth get() = _firebaseAuth

    override val user get() = _firebaseAuth.currentUser

    override val userId get() = user?.uid

    override fun signOut() {
        _firebaseAuth.signOut()
    }
}