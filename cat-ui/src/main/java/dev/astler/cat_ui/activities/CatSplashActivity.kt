package dev.astler.cat_ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.astler.unlib.gAppConfig

open class CatSplashActivity : AppCompatActivity() {

    open val mPackageName: String by lazy {
        gAppConfig.mMainActivityPackage.ifEmpty {
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
