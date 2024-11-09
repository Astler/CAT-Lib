package dev.astler.catlib.signin.hilt

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import dev.astler.catlib.config.AppConfig
import dev.astler.catlib.preferences.PreferencesTool
import dev.astler.catlib.signin.SignInManager
import dev.astler.catlib.signin.data.IFirebaseAuthRepository

@Module
@InstallIn(ActivityComponent::class)
object SignInActivityModule {

    @Provides
    @ActivityScoped
    fun provideSignInTool(
        @ActivityContext context: Context,
        preferences: PreferencesTool,
        firebaseAuthRepository: IFirebaseAuthRepository,
        appConfig: AppConfig
    ): SignInManager =
        SignInManager(context, firebaseAuthRepository, appConfig)

}