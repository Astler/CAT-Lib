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
import dev.astler.catlib.remote_config.RemoteConfigProvider
import dev.astler.catlib.signin.SignInTool
import dev.astler.catlib.signin.data.FirebaseAuthRepository
import dev.astler.catlib.signin.data.IFirebaseAuthRepository

@Module
@InstallIn(ActivityComponent::class)
object SignInToolModule {

    @Provides
    @ActivityScoped
    fun provideFirebaseAuthRepository(@ActivityContext context: Context): IFirebaseAuthRepository =
        FirebaseAuthRepository(context)

    @Provides
    @ActivityScoped
    fun provideSignInTool(@ActivityContext context: Context, preferences: PreferencesTool, remoteConfigProvider: RemoteConfigProvider,
                       appConfig: AppConfig, firebaseAuthRepository: IFirebaseAuthRepository): SignInTool =
        SignInTool(context, preferences, remoteConfigProvider, firebaseAuthRepository, appConfig)

}