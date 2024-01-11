package dev.astler.unli

import android.content.Context
import dev.astler.ads.AdsTool
import dev.astler.catlib.config.AppConfig
import dev.astler.catlib.preferences.PreferencesTool
import dev.astler.catlib.remote_config.RemoteConfigProvider
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.junit.Assert.assertNotNull

@RunWith(MockitoJUnitRunner::class)
class AdsToolTest {

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var preferences: PreferencesTool

    @Mock
    private lateinit var remoteConfig: RemoteConfigProvider

    @Mock
    private lateinit var appConfig: AppConfig

    private lateinit var adsTool: AdsTool

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        adsTool = AdsTool(context, preferences, remoteConfig, appConfig)
    }

    @Test
    fun testAdsToolInstanceIsNotNull() {
        assertNotNull(adsTool)
    }
}
