package dev.astler.unlib_test.fragments.menu

import androidx.compose.runtime.Composable
import dev.astler.unlib_compose.data.IFlexibleItem
import dev.astler.unlib_compose.interfaces.IComposeItem
import dev.astler.unlib_compose.ui.compose.flexible_grid.FlexibleGrid
import dev.astler.unlib_compose.ui.compose.items.BaseCard
import dev.astler.unlib_compose.ui.mixed.CatComposeFragment
import dev.astler.unlib_test.R
import dev.astler.unlib_test.data.TestBaseItem

abstract class TestsMenuFragment : CatComposeFragment() {

    open val menuItems = listOf<IComposeItem<IFlexibleItem, String>>(
        BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 2)),
        BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 2)),
        BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 2)),
        BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 3, true)),
        BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 3)),
        BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 6)),
        BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 4, false)),
        BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 2)),
        BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 6)),
    )

    open fun menuItemClicked(uid: String) {

    }

    @Composable
    override fun ScreenContent() {
        FlexibleGrid(items = menuItems, onItemClick = ::menuItemClicked)
    }
}