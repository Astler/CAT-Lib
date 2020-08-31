package dev.astler.unli

import android.content.Context
import androidx.multidex.MultiDexApplication
import dev.astler.unli.utils.log

val preferencesTool: PreferencesTool by lazy {
    UnliApp.prefs
}

open class UnliApp : MultiDexApplication() {

    companion object {
        lateinit var prefs: PreferencesTool

        private lateinit var applicationInstance: UnliApp

        @Synchronized
        fun getInstance(): UnliApp {
            return applicationInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
        prefs = PreferencesTool(this)
        applicationInstance = this
    }

    fun initAppLanguage(context: Context) {
        AppSettings.loadLocale(
                context
        )
    }
}
