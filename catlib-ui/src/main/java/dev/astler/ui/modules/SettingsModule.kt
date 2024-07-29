package dev.astler.ui.modules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.astler.ui.data.settings.Settings
import dev.astler.ui.data.settings.SettingsImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {
    @Binds
    @Singleton
    abstract fun bindUserSettings(
        userSettingsImpl: SettingsImpl
    ): Settings
}
