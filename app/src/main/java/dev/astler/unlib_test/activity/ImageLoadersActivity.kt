package dev.astler.unlib_test.activity

import android.os.Bundle
import android.view.LayoutInflater
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.cat_ui.activities.CatActivity
import dev.astler.catlib.utils.getBitmapFromAsset
import dev.astler.cat_ui.utils.toNoFilterDrawable
import dev.astler.cat_ui.utils.views.loadWithBackground
import dev.astler.unlib_test.R
import dev.astler.unlib_test.databinding.ActivityImagesBinding

@AndroidEntryPoint
class ImageLoadersActivity : CatActivity<ActivityImagesBinding>() {

    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityImagesBinding {
        return ActivityImagesBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.coilImage.load("https://hddesktopwallpapers.in/wp-content/uploads/2015/09/pink-flower-abstract.jpg")

        binding.coilRoundImage.load("https://hddesktopwallpapers.in/wp-content/uploads/2015/09/pink-flower-abstract.jpg") {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
            transformations(CircleCropTransformation())
        }

        binding.coilBlurImage.load("https://hddesktopwallpapers.in/wp-content/uploads/2015/09/pink-flower-abstract.jpg") {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
//            transformations(BlurTransformation(this@ImageLoadersActivity))
        }

        binding.roundedImage.load("https://hddesktopwallpapers.in/wp-content/uploads/2015/09/pink-flower-abstract.jpg") {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
            transformations(RoundedCornersTransformation(8f, 16f, 24f, 32f))
        }

        binding.grayScaleImage.load("https://hddesktopwallpapers.in/wp-content/uploads/2015/09/pink-flower-abstract.jpg") {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
//            transformations(GrayscaleTransformation())
        }

        binding.assetsImage.setImageBitmap(getBitmapFromAsset("frozen.png"))

        getBitmapFromAsset("frozen.png")?.let {
            binding.noFilterBitmapAssetsImage.setImageDrawable(it.toNoFilterDrawable(this))
        }

        getBitmapFromAsset("frozen.png")?.let {
            binding.coloredNoFilterBitmapAssetsImage.setImageDrawable(it.toNoFilterDrawable(this, R.color.colorPrimary))
        }

        binding.backgroundImage.loadWithBackground(
            "https://static.wikia.nocookie.net/minecraft_ru_gamepedia/images/4/46/Изумруд.png",
            dev.astler.catlib.core.R.color.fab_color
        )
    }
}
