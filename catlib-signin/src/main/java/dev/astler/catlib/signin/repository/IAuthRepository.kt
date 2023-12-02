package dev.astler.catlib.signin.repository

import dev.astler.catlib.model.State
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {
    fun isUserAuthenticatedInFirebase(): Flow<State<Boolean>>
}