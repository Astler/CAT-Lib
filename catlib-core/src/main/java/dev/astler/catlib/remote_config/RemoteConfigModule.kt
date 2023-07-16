package dev.astler.catlib.remote_config

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteConfigModule {

    @Provides
    @Singleton
    fun provideFirebaseRemoteConfig(@ApplicationContext context: Context): RemoteConfigProvider =
        RemoteConfigProvider(context)
}