package dev.astler.unli.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import dev.astler.unli.AppSettings
import dev.astler.unli.appPrefs

abstract class BaseUnLiActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        if (newBase.appPrefs.useEnglish) {
            super.attachBaseContext(AppSettings.loadLocale(newBase))
        } else {
            super.attachBaseContext(newBase)
        }
    }

//    fun updateBackgroundColor(color: Int = appPrefs.backgroundColor) {
//        window.decorView.setBackgroundColor(color)
//    }
//
//    fun updateActionbarColor(color: Int = appPrefs.primaryColor) {
//        supportActionBar?.setBackgroundDrawable(ColorDrawable(color))
//        updateActionBarTitle(supportActionBar?.title.toString(), color)
//        updateStatusbarColor(color)
//        //setTaskDescription(ActivityManager.TaskDescription(null, null, color))
//    }
//
//    fun updateStatusbarColor(color: Int) {
//        window.statusBarColor = color.darkenColor()
//    }

}