package dev.astler.unlib_test.fragments.menu

import androidx.navigation.fragment.findNavController
import com.ao.subscribeme.R
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.ui.compose.items.BaseCard
import dev.astler.unlib_test.data.TestBaseItem

@AndroidEntryPoint
class FragmentsMenuFragment: TestsMenuFragment() {

    private val settingsLocal = "settingsFragment"

    override val menuItems = listOf(
        BaseCard(TestBaseItem(gg.pressf.resources.R.string.settings, R.drawable.ic_launcher_foreground, 2, uid = settingsLocal)),
    )

    override fun menuItemClicked(uid: String) {
        when (uid) {
            settingsLocal -> {
                findNavController().navigate(R.id.action_global_settingsFragment)
            }

        }
    }
}