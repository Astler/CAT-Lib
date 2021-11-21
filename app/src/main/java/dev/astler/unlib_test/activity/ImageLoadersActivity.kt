package dev.astler.unlib_test.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import coil.transform.BlurTransformation
import coil.transform.CircleCropTransformation
import coil.transform.GrayscaleTransformation
import coil.transform.RoundedCornersTransformation
import dev.astler.unlib.utils.getBitmapFromAsset
import dev.astler.unlib.utils.toNoFilterDrawable
import dev.astler.unlib.utils.views.loadWithBackground
import dev.astler.unlib_test.R
import dev.astler.unlib_test.databinding.ActivityImagesBinding

class ImageLoadersActivity : AppCompatActivity() {

    private lateinit var mImagesBinding: ActivityImagesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mImagesBinding = ActivityImagesBinding.inflate(layoutInflater)

        setContentView(mImagesBinding.root)

        mImagesBinding.coilImage.load("https://hddesktopwallpapers.in/wp-content/uploads/2015/09/pink-flower-abstract.jpg")

        mImagesBinding.coilRoundImage.load("https://hddesktopwallpapers.in/wp-content/uploads/2015/09/pink-flower-abstract.jpg") {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
            transformations(CircleCropTransformation())
        }

        mImagesBinding.coilBlurImage.load("https://hddesktopwallpapers.in/wp-content/uploads/2015/09/pink-flower-abstract.jpg") {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
            transformations(BlurTransformation(this@ImageLoadersActivity))
        }

        mImagesBinding.roundedImage.load("https://hddesktopwallpapers.in/wp-content/uploads/2015/09/pink-flower-abstract.jpg") {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
            transformations(RoundedCornersTransformation(8f, 16f, 24f, 32f))
        }

        mImagesBinding.grayScaleImage.load("https://hddesktopwallpapers.in/wp-content/uploads/2015/09/pink-flower-abstract.jpg") {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
            transformations(GrayscaleTransformation())
        }

        mImagesBinding.assetsImage.setImageBitmap(getBitmapFromAsset("frozen.png"))

        getBitmapFromAsset("frozen.png")?.let {
            mImagesBinding.noFilterBitmapAssetsImage.setImageDrawable(it.toNoFilterDrawable(this))
        }

        getBitmapFromAsset("frozen.png")?.let {
            mImagesBinding.coloredNoFilterBitmapAssetsImage.setImageDrawable(it.toNoFilterDrawable(this, R.color.colorPrimary))
        }

        mImagesBinding.backgroundImage.loadWithBackground(
            "https://static.wikia.nocookie.net/minecraft_ru_gamepedia/images/4/46/Изумруд.png",
            R.color.fab_color
        )
    }
}
