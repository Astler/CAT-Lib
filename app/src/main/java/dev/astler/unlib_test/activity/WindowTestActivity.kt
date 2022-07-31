package dev.astler.unlib_test.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.astler.cat_ui.activities.CatActivity
import dev.astler.cat_ui.utils.setStatusBarColor
import dev.astler.cat_ui.utils.setSystemBarTransparent
import dev.astler.unlib_test.R
import dev.astler.unlib_test.databinding.ActivityImagesBinding

class WindowTestActivity : CatActivity() {

    private lateinit var mImagesBinding: ActivityImagesBinding

    private var mBarTransparent = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mImagesBinding = ActivityImagesBinding.inflate(layoutInflater)

        setContentView(mImagesBinding.root)

        mImagesBinding.root.setOnClickListener {
            mBarTransparent = !mBarTransparent

            if (mBarTransparent)
                setSystemBarTransparent()
            else
                setStatusBarColor(R.color.transparent)
        }

        setStatusBarColor(R.color.orange_dark)
    }
}
