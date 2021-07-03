package dev.astler.unlib_test

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import dev.astler.unlib.LocalStorage
import dev.astler.unlib.utils.infoLog
import dev.astler.unlib.view.ShortCodeTextView
import dev.astler.unlib.view.SplashView
import dev.astler.unlib_ads.activity.UnLibAdsActivity
import dev.astler.unlib_test.activity.ads.AdsTestMenu
import kotlinx.coroutines.launch

class MainActivity : UnLibAdsActivity() {

    companion object {
        private const val TARGET_SCAlE = 1f
        private const val SCALE_ANIMATION_DELAY = 800L
        private const val SCALE_ANIMATION_DURATION = 800L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<SplashView>(R.id.splash_view)
            .animateLogo()

        findViewById<View>(R.id.test)
            .animate()
            .scaleX(TARGET_SCAlE)
            .scaleY(TARGET_SCAlE)
            .setStartDelay(SCALE_ANIMATION_DELAY)
            .setDuration(SCALE_ANIMATION_DURATION)
            .start()

        val nText = """
            Okay. Let's try that! [img src=file/]
            New I will add few similar files [img src=file tint=#FF0000/][img src=file tint=#00FF00/][img src=file tint=#0000FF/]
            And now - error flag = [img src=faile/] and error flag with color [img src=faile tint=#00FFFF/]

        """.trimIndent()

        lifecycleScope.launch {
            infoLog("AAAAAAAAAAAAAAAAAAAAAAAAAAA!")

            infoLog("Start? = ${LocalStorage.startupTimes()}")
        }

        lifecycleScope.launch {
            infoLog("BBBBBBBBBBBBBBBBBB!")
            LocalStorage.incrementStartupCounter()
        }

        findViewById<ShortCodeTextView>(R.id.test).text = nText

        // findViewById<ShortCodeTextView>(R.id.test).setCompoundDrawables(null, getDrawable(R.drawable.btn_clear), null, null)

        findViewById<ShortCodeTextView>(R.id.test).setOnClickListener {
            lifecycleScope.launch {
                LocalStorage.incrementStartupCounter()
            }
            // startActivity(Intent(this, TestMenu::class.java))
        }

        findViewById<ShortCodeTextView>(R.id.test).setOnLongClickListener {
            startActivity(Intent(this, AdsTestMenu::class.java))
            true
        }
    }
}
