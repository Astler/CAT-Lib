package dev.astler.unlib_test.fragments.menu

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.ao.subscribeme.R
import dev.astler.ui.compose.items.BaseCard
import dev.astler.unlib_test.data.TestBaseItem

@AndroidEntryPoint
class ComposeMenuFragment : TestsMenuFragment() {

    private val dynamicComposeKey = "DynamicCompose"
    private val typewriterKey = "Typewriter"
    private val collapsingToolbarKey = "CollapsingToolbar"

    override val menuItems = listOf(
        BaseCard(TestBaseItem(R.string.static_grid, R.drawable.ic_launcher_foreground, 3, uid = dynamicComposeKey)),
        BaseCard(TestBaseItem(R.string.typewriter, R.drawable.ic_launcher_foreground, 3, uid = typewriterKey)),
        BaseCard(TestBaseItem(R.string.collapsing_test, R.drawable.ic_launcher_foreground, 6, uid = collapsingToolbarKey)),
    )

    override fun menuItemClicked(uid: String) {
        when (uid) {
            dynamicComposeKey -> {
                findNavController().navigate(R.id.action_global_composeGridMaterial3PreviewFragment)
            }

            typewriterKey -> {
                findNavController().navigate(R.id.action_global_typewriterComposeFragment)
            }

            collapsingToolbarKey -> {
                findNavController().navigate(R.id.action_global_collapsingToolbarFragment)
            }
        }
    }
}