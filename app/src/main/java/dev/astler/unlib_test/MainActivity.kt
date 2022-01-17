package dev.astler.unlib_test

import android.content.Intent
import android.os.Bundle
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dev.astler.cat_ui.activities.CatActivity
import dev.astler.unlib.utils.toast
import dev.astler.unlib_test.activity.TestMenu
import dev.astler.unlib_test.activity.ads.AdsTestMenu
import dev.astler.unlib_test.databinding.ActivityMainBinding

class MainActivity : CatActivity() {

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

        mViewBinding.splashView.animateLogo()

        mViewBinding.test
            .animate()
            .scaleX(TARGET_SCAlE)
            .scaleY(TARGET_SCAlE)
            .setStartDelay(SCALE_ANIMATION_DELAY)
            .setDuration(SCALE_ANIMATION_DURATION)
            .start()

        val nText = """ Папка: [ft text=ASTLER = Vlad params=~tf_b/]
            😶‍🌫️, 🧔🏻‍♀️, 🧑🏿‍❤️‍🧑🏾
            Okay. Let's try that! [img src=file/]
            New I will add few similar files [img src=file tint=#FF0000/][img src=file tint=#00FF00/][img src=file tint=#0000FF/]
            And now - error flag = [img src=faile/] and error flag with color [img src=faile tint=#00FFFF/]

        """.trimIndent()

        mViewBinding.test.text = nText

        // mViewBinding.test.setCompoundDrawables(null, getDrawable(R.drawable.btn_clear), null, null)

        mViewBinding.test.setOnClickListener {
            startActivity(Intent(this, TestMenu::class.java))
        }

        mViewBinding.test.setOnLongClickListener {
            startActivity(Intent(this, AdsTestMenu::class.java))
            true
        }

//        mViewBinding.test.setOnLongClickListener {
//            startActivity(Intent(this, AdsTestMenu::class.java))
//            true
//        }

        Firebase.messaging.subscribeToTopic("weather")
            .addOnCompleteListener { task ->
                var msg = "MESSAGE"
                if (!task.isSuccessful) {
                    msg += " Not Good"
                }
                toast(msg)
            }
    }
}
