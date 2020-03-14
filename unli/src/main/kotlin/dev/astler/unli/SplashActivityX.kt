package dev.astler.unli

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class SplashActivityX : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setMainActivity()?.let {
            startActivity(Intent(this, setMainActivity()))
        }

        splashTasks()

        finish()
    }

    open fun setMainActivity(): Class<*>? = null

    open fun splashTasks() {

    }
}