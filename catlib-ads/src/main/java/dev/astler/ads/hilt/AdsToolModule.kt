package dev.astler.ads.hilt

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import dev.astler.ads.initialization.AdsTool
import dev.astler.catlib.preferences.PreferencesTool
import dev.astler.catlib.remote_config.RemoteConfigProvider

@Module
@InstallIn(ActivityComponent::class)
object AdsToolModule {

    @Provides
    @ActivityScoped
    fun provideAdsTool(@ActivityContext context: Context, preferences: PreferencesTool, remoteConfigProvider: RemoteConfigProvider): AdsTool =
        AdsTool(context, preferences, remoteConfigProvider)
}