package dev.astler.unlib

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.* // ktlint-disable no-wildcard-imports
import dev.astler.unlib.PreferencesKeys.ADS_DISABLED_KEY
import dev.astler.unlib.PreferencesKeys.APP_LANGUAGE_KEY
import dev.astler.unlib.PreferencesKeys.APP_START_KEY
import dev.astler.unlib.PreferencesKeys.APP_START_TIME_KEY
import dev.astler.unlib.PreferencesKeys.APP_THEME_KEY
import dev.astler.unlib.PreferencesKeys.FIRST_START_KEY
import dev.astler.unlib.PreferencesKeys.LAST_AD_TIME_KEY
import dev.astler.unlib.PreferencesKeys.NO_ADS_DAY_KEY
import dev.astler.unlib.PreferencesKeys.REVIEW_REQUEST_TIME_KEY
import dev.astler.unlib.PreferencesKeys.START_AD_KEY
import dev.astler.unlib.PreferencesKeys.TEXT_SIZE_KEY
import dev.astler.unlib.PreferencesKeys.VIBRATION_KEY
import dev.astler.unlib.utils.infoLog
import kotlinx.coroutines.flow.* // ktlint-disable no-wildcard-imports
import java.io.IOException
import java.util.* // ktlint-disable no-wildcard-imports

const val cTextSizeDefault = 18f
const val cSystemDefault = "system"
const val cDark = "dark"

object PreferencesKeys {
    const val PREFERENCE_NAME = "preferences_un_lib"

    val APP_START_KEY = intPreferencesKey("appStart")
    val NO_ADS_DAY_KEY = intPreferencesKey("dayWithoutAds")

    val REVIEW_REQUEST_TIME_KEY = longPreferencesKey("reviewRequest")
    val START_AD_KEY = longPreferencesKey("startAdTime")
    val APP_START_TIME_KEY = longPreferencesKey("startTime")
    val LAST_AD_TIME_KEY = longPreferencesKey("lastAdTime")

    val TEXT_SIZE_KEY = floatPreferencesKey("textSizeNew")

    val APP_THEME_KEY = stringPreferencesKey("appTheme")
    val APP_LANGUAGE_KEY = stringPreferencesKey("appLocale")

    val VIBRATION_KEY = booleanPreferencesKey("keyVibration")
    val FIRST_START_KEY = booleanPreferencesKey("firstStart")
    val ADS_DISABLED_KEY = booleanPreferencesKey("noAdsPref")

    val AGE_CONFIRMED_ADS = booleanPreferencesKey("age_confirmed")
    val CHILD_ADS = booleanPreferencesKey("child_ads")
}

object LocalStorage {

    val dataStore = UnliApp.getInstance().mDataStorePreferences

    suspend fun textSize(): Float = readDirectValue(TEXT_SIZE_KEY, 18f)
    suspend fun textSizeWatcher(pAction: (pValue: Float) -> Unit) {
        readValue(TEXT_SIZE_KEY, 18f) {
            pAction.invoke(this)
        }
    }
    suspend fun noAdsDayWatcher(pAction: (pValue: Int) -> Unit) {
        readValue(NO_ADS_DAY_KEY, -1) {
            pAction.invoke(this)
        }
    }

    suspend fun appThemeWatcher(pAction: (pValue: String) -> Unit) {
        readValue(APP_THEME_KEY, cSystemDefault) {
            pAction.invoke(this)
        }
    }

    suspend fun appLanguageWatcher(pAction: (pValue: String) -> Unit) {
        readValue(APP_LANGUAGE_KEY, cSystemDefault) {
            pAction.invoke(this)
        }
    }

    suspend fun appTheme(): String = readDirectValue(APP_THEME_KEY, cSystemDefault)
    suspend fun appLanguage(): String = readDirectValue(APP_LANGUAGE_KEY, cSystemDefault)

    suspend fun vibrationActive(): Boolean = readDirectValue(VIBRATION_KEY, false)
    suspend fun firstStart(): Boolean = readDirectValue(FIRST_START_KEY, true)
    suspend fun firstStartForVersion(pVersion: Int): Boolean = readDirectValue(booleanPreferencesKey("firstStartVersion$pVersion"), true)
    suspend fun isDarkTheme(): Boolean = appTheme() == cDark

    suspend fun adsDisabled(): Boolean = readDirectValue(ADS_DISABLED_KEY, false)

    suspend fun startupTimes() = readDirectValue(APP_START_KEY, 0)
    suspend fun noAdsDay() = readDirectValue(NO_ADS_DAY_KEY, -1)

    suspend fun reviewLastRequest() = readDirectValue(REVIEW_REQUEST_TIME_KEY, 0L)
    suspend fun startAdTime() = readDirectValue(START_AD_KEY, 0L)

    suspend fun incrementStartupCounter() {
        dataStore.edit { settings ->
            val currentCounterValue = settings[APP_START_KEY] ?: 0
            infoLog("Increment! $currentCounterValue + 1")
            settings[APP_START_KEY] = currentCounterValue + 1
        }
    }

    suspend fun incrementThemeCounter() {
        dataStore.edit { settings ->
            val currentCounterValue = settings[APP_THEME_KEY] ?: cSystemDefault
            infoLog("Increment theme!! $currentCounterValue + 1")

            settings[APP_THEME_KEY] = if (currentCounterValue == cSystemDefault || currentCounterValue == cDark) "light" else "dark"
        }
    }

    suspend fun firstStartForVersionComplete(pVersion: Int) {
        storeValue(booleanPreferencesKey("firstStartVersion$pVersion"), false)
    }

    suspend fun setStartTime(pTime: Long) {
        storeValue(APP_START_TIME_KEY, pTime)
    }

    suspend fun setReviewRequestTime(pTime: Long) {
        storeValue(REVIEW_REQUEST_TIME_KEY, pTime)
    }

    suspend fun setStartAdTime(pTime: Long) {
        storeValue(START_AD_KEY, pTime)
    }

    suspend fun setLastAdTime(pTime: Long) {
        storeValue(LAST_AD_TIME_KEY, pTime)
    }

    suspend inline fun <reified T> storeValue(key: Preferences.Key<T>, value: Any) {
        dataStore.edit {
            it[key] = value as T
        }
    }

    suspend inline fun <reified T> readValue(
        key: Preferences.Key<T>,
        pDefaultValue: T,
        crossinline responseFunc: T.() -> Unit
    ) {
        dataStore.getFromLocalStorage(key, pDefaultValue) {
            responseFunc.invoke(this)
        }
    }

    suspend inline fun <reified T> readDirectValue(
        key: Preferences.Key<T>,
        pDefaultValue: T
    ): T {
        return dataStore.getDirectValue(key, pDefaultValue)
    }

    suspend fun setNoDayAds() {
        storeValue(NO_ADS_DAY_KEY, GregorianCalendar.getInstance().get(GregorianCalendar.DATE))
    }

    suspend inline fun readDirectLongValue(
        pKey: String,
        pDefaultValue: Long = 0L
    ): Long {
        return dataStore.getDirectValue(longPreferencesKey(pKey), pDefaultValue)
    }

    suspend inline fun readLongValue(
        pKey: String,
        pDefaultValue: Long = 0L,
        crossinline responseFunc: Long.() -> Unit
    ) {
        dataStore.getFromLocalStorage(longPreferencesKey(pKey), pDefaultValue) {
            responseFunc.invoke(this)
        }
    }
}

suspend inline fun <reified T> DataStore<Preferences>.getFromLocalStorage(
    PreferencesKey: Preferences.Key<T>,
    pDefaultValue: T,
    crossinline func: T.() -> Unit
) {
    infoLog("in getFromLocalStorage")

    data.catch {
        infoLog("catch! = $it")

        if (it is IOException) {
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map {
        it[PreferencesKey]
    }.collect {
        infoLog("detect value = $it")

        if (it == null) {
            func.invoke(pDefaultValue)
        } else {
            if (PreferencesKey == TEXT_SIZE_KEY) {
                func.invoke(it.toString().toFloat() as T)
            } else func.invoke(it as T)
        }
    }
}

suspend inline fun <reified T> DataStore<Preferences>.getDirectValue(
    PreferencesKey: Preferences.Key<T>,
    pDefaultValue: T
): T {
    infoLog("in getFromLocalStorage")

    val nValue = data.catch {
        infoLog("catch! = $it")

        if (it is IOException) {
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map {
        it[PreferencesKey]
    }.first() ?: pDefaultValue

    return if (PreferencesKey == TEXT_SIZE_KEY) {
        nValue.toString().toFloat() as T
    } else nValue
}
