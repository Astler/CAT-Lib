package dev.astler.unlib.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.astler.unlib.gAppConfig

open class SplashActivityUnLib : AppCompatActivity() {

    open val mPackageName: String by lazy {
        if (gAppConfig.mMainActivityPackage.isNotEmpty()) {
            gAppConfig.mMainActivityPackage
        } else {
            packageName
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(Intent.ACTION_VIEW).setClassName(packageName, "$mPackageName.MainActivity"))
        splashTasks()
        overridePendingTransition(0, 0)
        finish()
    }

    open fun splashTasks() {}
}
