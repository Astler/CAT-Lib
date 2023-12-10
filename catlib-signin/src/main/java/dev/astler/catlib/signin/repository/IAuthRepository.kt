package dev.astler.catlib.signin.repository

import com.google.firebase.auth.FirebaseUser
import dev.astler.catlib.model.State
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {
    fun isUserAuthenticatedInFirebase(): Flow<State<Boolean>>
    fun firebaseUser(): Flow<State<FirebaseUser?>>
}