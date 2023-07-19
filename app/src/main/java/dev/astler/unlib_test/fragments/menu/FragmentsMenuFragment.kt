package dev.astler.unlib_test.fragments.menu

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.unlib_compose.ui.compose.items.BaseCard
import dev.astler.unlib_test.R
import dev.astler.unlib_test.data.TestBaseItem
import dev.astler.unlib_test.fragments.menu.TestsMenuFragment

@AndroidEntryPoint
class FragmentsMenuFragment: TestsMenuFragment() {

    private val shortCodePreviewKey = ""

    override val menuItems = listOf(
        BaseCard(TestBaseItem(R.string.short_code_view, R.drawable.ic_launcher_foreground, 2, uid = shortCodePreviewKey)),
    )

    override fun menuItemClicked(uid: String) {
        when (uid) {
            shortCodePreviewKey -> {
                findNavController().navigate(R.id.action_global_shortCodePreviewFragment)
            }
        }
    }
}