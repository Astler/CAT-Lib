package dev.astler.unli_text

import android.os.Bundle
import android.view.View
import dev.astler.unlib.ui.activity.BaseUnLiActivity
import dev.astler.unlib.view.ShortCodeTextView
import dev.astler.unlib.view.SplashView

class MainActivity : BaseUnLiActivity() {

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

        findViewById<ShortCodeTextView>(R.id.test).text = nText
    }
}