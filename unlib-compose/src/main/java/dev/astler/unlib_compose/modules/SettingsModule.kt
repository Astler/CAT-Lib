package dev.astler.unlib_compose.modules

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.astler.unlib_compose.data.settings.Settings
import dev.astler.unlib_compose.data.settings.SettingsImpl
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import javax.inject.Singleton

//@Module
//@InstallIn(SingletonComponent::class)
//abstract class SettingsModule {
//    @Binds
//    @Singleton
//    abstract fun bindUserSettings(
//        userSettingsImpl: SettingsImpl
//    ): Settings
//}
