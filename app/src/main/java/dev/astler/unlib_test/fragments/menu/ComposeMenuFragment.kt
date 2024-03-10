package dev.astler.unlib_test.fragments.menu

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.unlib_compose.ui.compose.items.BaseCard
import com.ao.subscribeme.R
import dev.astler.unlib_test.data.TestBaseItem

@AndroidEntryPoint
class ComposeMenuFragment : TestsMenuFragment() {

    private val dynamicComposeKey = "DynamicCompose"
    private val typewriterKey = "Typewriter"
    private val collapsingToolbarKey = "CollapsingToolbar"
    private val staticGridKey = "StaticGridKey"

    override val menuItems = listOf(
        BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 2, uid = dynamicComposeKey)),
        BaseCard(TestBaseItem(R.string.typewriter, R.drawable.ic_launcher_foreground, 2, uid = typewriterKey)),
        BaseCard(TestBaseItem(R.string.typewriter, R.drawable.ic_launcher_foreground, 6, uid = collapsingToolbarKey)),
        BaseCard(TestBaseItem(R.string.static_grid, R.drawable.ic_launcher_foreground, 6, uid = staticGridKey)),
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

            staticGridKey -> {
                findNavController().navigate(R.id.action_global_staticGridComposeFragment)
            }
        }
    }
}