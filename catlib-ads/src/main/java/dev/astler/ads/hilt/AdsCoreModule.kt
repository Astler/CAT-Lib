package dev.astler.ads.hilt

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.astler.catlib.preferences.PreferencesTool
import dev.astler.ads.initialization.AdsCore
import dev.astler.catlib.config.AppConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AdsCoreModule {

    @Provides
    @Singleton
    fun provideAdsCore(@ApplicationContext context: Context, preferences: PreferencesTool, appConfig: AppConfig): AdsCore =
        AdsCore(context, preferences, appConfig)
}