package dev.astler.unlib_test.fragments.menu

import androidx.navigation.fragment.findNavController
import com.ao.subscribeme.R
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.ui.compose.items.BaseCard
import dev.astler.unlib_test.data.TestBaseItem

@AndroidEntryPoint
class ViewsMenuFragment: TestsMenuFragment() {

    private val shortCodePreviewKey = "ShortCodePreview"
    private val imageLoadKey = "ImageLoad"

    override val menuItems = listOf(
        BaseCard(TestBaseItem(R.string.short_code_view, R.drawable.ic_launcher_foreground, 3, uid = shortCodePreviewKey)),
        BaseCard(TestBaseItem(R.string.image_load, R.drawable.ic_launcher_foreground, 3, uid = imageLoadKey)),
    )

    override fun menuItemClicked(uid: String) {
        when (uid) {
            shortCodePreviewKey -> {
                findNavController().navigate(R.id.action_global_shortCodePreviewFragment)
            }

            imageLoadKey -> {
                findNavController().navigate(R.id.action_global_imageLoadVariantsFragment)
            }
        }
    }
}