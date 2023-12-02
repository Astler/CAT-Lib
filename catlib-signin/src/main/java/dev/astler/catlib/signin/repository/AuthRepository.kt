package dev.astler.catlib.signin.repository

import com.google.firebase.auth.FirebaseAuth
import dev.astler.catlib.model.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
) : IAuthRepository {
    override fun isUserAuthenticatedInFirebase(): Flow<State<Boolean>> = flow {
        try {
            emit(State.loading())
            if (auth.currentUser != null) {
                emit(State.success(true))
            } else {
                emit(State.success(false))
            }
        } catch (e: Exception) {
            emit(State.success(false))
        }
    }

}