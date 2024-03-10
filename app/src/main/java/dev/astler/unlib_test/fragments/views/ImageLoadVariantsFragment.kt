package dev.astler.unlib_test.fragments.views

import android.os.Bundle
import android.view.View
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import dev.astler.cat_ui.fragments.CatFragment
import dev.astler.cat_ui.utils.toNoFilterDrawable
import dev.astler.cat_ui.utils.views.loadWithBackground
import dev.astler.catlib.constants.MockImageUrl
import dev.astler.catlib.extensions.getBitmapFromAsset
import com.ao.subscribeme.R
import com.ao.subscribeme.databinding.ActivityImagesBinding

class ImageLoadVariantsFragment: CatFragment<ActivityImagesBinding>(ActivityImagesBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.coilImage.load(MockImageUrl)

        binding.coilRoundImage.load(MockImageUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
            transformations(CircleCropTransformation())
        }

        binding.coilBlurImage.load(MockImageUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
//            transformations(BlurTransformation(this@ImageLoadersActivity))
        }

        binding.roundedImage.load(MockImageUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
            transformations(RoundedCornersTransformation(8f, 16f, 24f, 32f))
        }

        binding.grayScaleImage.load(MockImageUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
//            transformations(GrayscaleTransformation())
        }

        binding.assetsImage.setImageBitmap(safeContext.getBitmapFromAsset("frozen.png"))

        safeContext.getBitmapFromAsset("frozen.png")?.let {
            binding.noFilterBitmapAssetsImage.setImageDrawable(it.toNoFilterDrawable(safeContext))
        }

        safeContext.getBitmapFromAsset("frozen.png")?.let {
            binding.coloredNoFilterBitmapAssetsImage.setImageDrawable(it.toNoFilterDrawable(safeContext, R.color.colorPrimary))
        }

        binding.backgroundImage.loadWithBackground(
            "https://static.wikia.nocookie.net/minecraft_ru_gamepedia/images/4/46/Изумруд.png",
            dev.astler.catlib.core.R.color.fab_color
        )
    }
}