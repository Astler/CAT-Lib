package dev.astler.catlib.remote_config

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
class RemoteConfigModule {

    @Provides
    @ActivityScoped
    fun provideFirebaseRemoteConfig(@ActivityContext context: Context): RemoteConfigProvider = RemoteConfigProvider(context)
}