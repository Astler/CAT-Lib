package dev.astler.catlib.analytics

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.astler.catlib.config.AppConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AnalyticsModule() {

    @Provides
    @Singleton
    fun provideCatAnalytics(@ApplicationContext context: Context, appConfig: AppConfig): CatAnalytics =
        CatAnalytics(context, appConfig)
}