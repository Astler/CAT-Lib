package dev.astler.unli_text

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import coil.transform.BlurTransformation
import coil.transform.CircleCropTransformation
import coil.transform.GrayscaleTransformation
import coil.transform.RoundedCornersTransformation
import dev.astler.unli_text.databinding.ActivityImagesBinding
import dev.astler.unlib.utils.loadWithBackground

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                transformations(BlurTransformation(this@ImageLoadersActivity))
            }
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

        mImagesBinding.backgroundImage.loadWithBackground("https://static.wikia.nocookie.net/minecraft_ru_gamepedia/images/4/46/Изумруд.png", R.color.fab_color)
    }
}
