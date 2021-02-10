package dev.astler.unlib.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class SplashActivityUnLib : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(Intent.ACTION_VIEW).setClassName(packageName, "${packageName}.MainActivity"))

        splashTasks()

        finish()
    }

    open fun splashTasks() {}
}