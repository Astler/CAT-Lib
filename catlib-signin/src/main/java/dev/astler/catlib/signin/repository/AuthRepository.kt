package dev.astler.catlib.signin.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
            firebaseUser().collect { userState ->
                when (userState) {
                    is State.Loading -> emit(State.loading())
                    is State.Success -> emit(State.success(userState.data != null))
                    is State.Error -> emit(State.error(userState.toString()))
                }
            }
        } catch (e: Exception) {
            emit(State.error(e.message))
        }
    }

    override fun firebaseUser(): Flow<State<FirebaseUser?>> = flow {
        try {
            emit(State.loading())
            val currentUser = auth.currentUser
            emit(State.success(currentUser))
        } catch (e: Exception) {
            emit(State.error(e.message))
        }
    }

}