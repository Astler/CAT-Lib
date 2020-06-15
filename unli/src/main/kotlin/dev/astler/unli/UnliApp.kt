package dev.astler.unli

import androidx.multidex.MultiDexApplication

val preferencesTool: PreferencesTool by lazy {
    UnliApp.prefs
}

class UnliApp : MultiDexApplication() {

    companion object {
        lateinit var prefs: PreferencesTool
    }

    override fun onCreate() {
        super.onCreate()
        prefs = PreferencesTool(applicationContext)
        //AppCompatDelegate.setDefaultNightMode(ThemeUtils.getDefaultNightMode(applicationContext))
    }
}
