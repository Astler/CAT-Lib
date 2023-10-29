package dev.astler.catlib.config

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.astler.catlib.core.R
import dev.astler.catlib.catJson
import dev.astler.catlib.extensions.readFileFromRaw
import kotlinx.serialization.decodeFromString
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppConfigModule {
    @Provides
    @Singleton
    fun providePreferences(@ApplicationContext context: Context): AppConfig {
        val appConfigFile = context.readFileFromRaw(R.raw.app_config)

        return if (appConfigFile.isNotEmpty()) {
            catJson.decodeFromString(appConfigFile)
        } else {
            AppConfig()
        }
    }
}