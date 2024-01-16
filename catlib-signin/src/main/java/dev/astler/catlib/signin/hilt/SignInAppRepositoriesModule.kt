package dev.astler.catlib.signin.hilt

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.astler.catlib.signin.data.FirebaseAuthRepository
import dev.astler.catlib.signin.data.IFirebaseAuthRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SignInAppRepositoriesModule {
    @Provides
    @Singleton
    fun provideFirebaseAuthRepository(@ApplicationContext context: Context): IFirebaseAuthRepository =
        FirebaseAuthRepository(context)
}