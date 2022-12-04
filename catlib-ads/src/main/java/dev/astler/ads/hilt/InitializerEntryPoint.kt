package dev.astler.ads.hilt

import android.content.Context
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import dev.astler.ads.initialization.AdsInitializer

@EntryPoint
@InstallIn(SingletonComponent::class) //<-- installing in the ApplicationComponent !!
interface InitializerEntryPoint {

    fun inject(adsInitializer: AdsInitializer)

    companion object {
        fun resolve(context: Context): InitializerEntryPoint {
            val appContext = context.applicationContext ?: throw IllegalStateException()
            return EntryPointAccessors.fromApplication(
                appContext,
                InitializerEntryPoint::class.java
            )
        }
    }
}