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
    }

    override fun attachBaseContext(newBase: Context) {
        prefs = PreferencesTool(newBase)

        super.attachBaseContext(AppSettings.loadLocale(newBase, newBase.appPrefs.useEnglish)?:newBase)
        log("test trest -> " + newBase.appPrefs.userLanguage)
    }
}
