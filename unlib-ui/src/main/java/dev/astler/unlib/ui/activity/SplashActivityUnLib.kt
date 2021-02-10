package dev.astler.unlib.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.astler.unlib.utils.infoLog

open class SplashActivityUnLib : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        infoLog("Start ${javaClass.name} from $packageName", "ForAstler:UNLI")
        super.onCreate(savedInstanceState)

        infoLog("start ${packageName}.MainActivity", "ForAstler:UNLI")
        startActivity(Intent(Intent.ACTION_VIEW).setClassName(packageName, "${packageName}.MainActivity"))


        infoLog("Perform splashTasks() in ${javaClass.name}", "ForAstler:UNLI")
        splashTasks()

        infoLog("Finish ${javaClass.name}", "ForAstler:UNLI")
        finish()
    }

    open fun splashTasks() {}
}