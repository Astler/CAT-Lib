package dev.astler.catlib.signin.repository

import com.google.firebase.auth.FirebaseUser
import dev.astler.catlib.model.CatState
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {
    fun isUserAuthenticatedInFirebase(): Flow<CatState<Boolean>>
    fun firebaseUser(): Flow<CatState<FirebaseUser?>>
}