package dev.astler.unlib_test

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.cat_ui.activities.CatActivity
import dev.astler.cat_ui.utils.getDimensionFromAttr
import dev.astler.catlib.gPreferencesTool
import dev.astler.catlib.getDefaultNightMode
import dev.astler.catlib.utils.toast
import dev.astler.unlib_test.activity.TestMenu
import dev.astler.unlib_test.databinding.ActivityMainBinding

@AndroidEntryPoint
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
        AppCompatDelegate.setDefaultNightMode(getDefaultNightMode())
        delegate.applyDayNight()

        mViewBinding.test
            .animate()
            .scaleX(TARGET_SCAlE)
            .scaleY(TARGET_SCAlE)
            .setStartDelay(SCALE_ANIMATION_DELAY)
            .setDuration(SCALE_ANIMATION_DURATION)
            .start()

        mViewBinding.test.text =
            getString(R.string.strange_string) + getDimensionFromAttr(androidx.appcompat.R.attr.actionBarSize) + "\n" + mViewBinding.toolbar.height


        // mViewBinding.test.setCompoundDrawables(null, getDrawable(R.drawable.btn_clear), null, null)

        mViewBinding.test.setOnClickListener {
            startActivity(Intent(this, TestMenu::class.java))
        }

//        gPreferencesTool.appTheme = "dark"
//        gPreferencesTool.appTheme = "light"
//        gPreferencesTool.appTheme = "system"
//        gPreferencesTool.appTheme = "auto"

        mViewBinding.test.setOnLongClickListener {
            if (gPreferencesTool.appTheme == "light") {
                gPreferencesTool.appTheme = "dark"
            } else {
                gPreferencesTool.appTheme = "light"
            }
            true
        }
//        mViewBinding.test.setOnLongClickListener {
//            if (ConfigurationCompat.getLocales(resources.configuration)[0]?.language?.contains("ru") == true) {
//                updateLocale(Locales.English)
//            } else {
//                updateLocale(Locales.Russian)
//            }
////            startActivity(Intent(this, AdsTestMenu::class.java))
//            true
//        }

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
