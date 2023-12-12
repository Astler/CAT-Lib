package dev.astler.catlib.signin.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dev.astler.catlib.model.CatState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
) : IAuthRepository {
    override fun isUserAuthenticatedInFirebase(): Flow<CatState<Boolean>> = flow {
        try {
            firebaseUser().collect { userState ->
                when (userState) {
                    is CatState.Loading -> emit(CatState.loading())
                    is CatState.Success -> emit(CatState.success(userState.data != null))
                    is CatState.Error -> emit(CatState.error(userState.toString()))
                }
            }
        } catch (e: Exception) {
            emit(CatState.error(e.message))
        }
    }

    override fun firebaseUser(): Flow<CatState<FirebaseUser?>> = flow {
        try {
            emit(CatState.loading())
            val currentUser = auth.currentUser
            emit(CatState.success(currentUser))
        } catch (e: Exception) {
            emit(CatState.error(e.message))
        }
    }

}