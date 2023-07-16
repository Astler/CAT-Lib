package dev.astler.unlib_test.fragments.menu

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.unlib_test.R
import dev.astler.unlib_test.data.TestBaseItem

@AndroidEntryPoint
class ComposeMenuFragment : TestsMenuFragment() {

    private val dynamicComposeKey = "DynamicCompose"
    private val typewriterKey = "Typewriter"

    override val menuItems = listOf(
        TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 2, uid = dynamicComposeKey),
        TestBaseItem(R.string.typewriter, R.drawable.ic_launcher_foreground, 2, uid = typewriterKey),
    )

    override fun menuItemClicked(uid: String) {
        when (uid) {
            dynamicComposeKey -> {
                findNavController().navigate(R.id.action_global_composeGridMaterial3PreviewFragment)
            }

            typewriterKey -> {
                findNavController().navigate(R.id.action_global_typewriterComposeFragment)
            }
        }
    }
}