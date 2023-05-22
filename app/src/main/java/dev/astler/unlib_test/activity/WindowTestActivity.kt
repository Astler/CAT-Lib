package dev.astler.unlib_test.activity

import android.os.Bundle
import android.view.LayoutInflater
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.cat_ui.activities.CatActivity
import dev.astler.cat_ui.utils.setStatusBarColor
import dev.astler.cat_ui.utils.setSystemBarTransparent
import dev.astler.unlib_test.databinding.ActivityImagesBinding

@AndroidEntryPoint
class WindowTestActivity : CatActivity<ActivityImagesBinding>() {

    private var mBarTransparent = false
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityImagesBinding {
        return ActivityImagesBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.root.setOnClickListener {
            mBarTransparent = !mBarTransparent

            if (mBarTransparent)
                setSystemBarTransparent()
            else
                setStatusBarColor(dev.astler.catlib.ui.R.color.transparent)
        }

        setStatusBarColor(dev.astler.catlib.ui.R.color.orange_dark)
    }
}
