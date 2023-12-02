package dev.astler.catlib.signin

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.astler.catlib.signin.repository.AuthRepository
import dev.astler.catlib.signin.repository.IAuthRepository

@Module
@InstallIn(SingletonComponent::class)
object SignInModule {
    @Provides
    fun provideFirebaseAuthInstance() = FirebaseAuth.getInstance()

    @Provides
    fun provideAuthRepository(
        auth: FirebaseAuth,
    ): IAuthRepository = AuthRepository(auth)
}