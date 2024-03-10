package dev.astler.unlib_test.fragments.menu

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.unlib_compose.ui.compose.items.BaseCard
import com.ao.subscribeme.R
import dev.astler.unlib_test.data.TestBaseItem
import dev.astler.unlib_test.fragments.menu.TestsMenuFragment

@AndroidEntryPoint
class FragmentsMenuFragment: TestsMenuFragment() {

    private val settingsLocal = "settingsFragment"
    private val settingsCat = "settingsFragmentCat"

    override val menuItems = listOf(
        BaseCard(TestBaseItem(R.string.short_code_view, R.drawable.ic_launcher_foreground, 2, uid = settingsLocal)),
        BaseCard(TestBaseItem(R.string.short_code_view, R.drawable.ic_launcher_foreground, 2, uid = settingsCat)),
    )

    override fun menuItemClicked(uid: String) {
        when (uid) {
            settingsLocal -> {
                findNavController().navigate(R.id.action_global_settingsFragment)
            }

            settingsCat -> {
                findNavController().navigate(R.id.action_global_catDefaultSettingsFragment)
            }
        }
    }
}