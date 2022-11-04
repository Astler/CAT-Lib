package dev.astler.unlib_test.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import dev.astler.cat_ui.activities.CatActivity
import dev.astler.unlib_test.R
import dev.astler.unlib_test.activity.ads.AdsOpenItemListActivity
import dev.astler.unlib_test.databinding.ActivityMainBinding

class ShortcodeTextActivity : CatActivity() {

    companion object {
        private const val TARGET_SCAlE = 1f
        private const val SCALE_ANIMATION_DELAY = 800L
        private const val SCALE_ANIMATION_DURATION = 800L
    }

    private lateinit var mViewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(mViewBinding.root)

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

        mViewBinding.test.text = nText

        mViewBinding.test.setOnClickListener {
            startActivity(Intent(this, ImageLoadersActivity::class.java))
        }

        mViewBinding.test.setOnLongClickListener {
            startActivity(Intent(this, AdsOpenItemListActivity::class.java))
            true
        }
    }
}
