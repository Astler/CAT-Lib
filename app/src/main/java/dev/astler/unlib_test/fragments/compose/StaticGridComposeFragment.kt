package dev.astler.unlib_test.fragments.compose

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.unlib_compose.ui.compose.collapse_toolbar.CollapsingToolbarContent
import dev.astler.unlib_compose.ui.compose.flexible_grid.FlexibleGrid
import dev.astler.unlib_compose.ui.compose.flexible_grid.StaticFlexibleGrid
import dev.astler.unlib_compose.ui.compose.items.BaseCard
import dev.astler.unlib_compose.ui.mixed.CatComposeFragment
import dev.astler.unlib_test.R
import dev.astler.unlib_test.data.TestBaseItem
import dev.astler.unlib_test.fragments.menu.TestsMenuFragment

@AndroidEntryPoint
class StaticGridComposeFragment: CatComposeFragment() {

    @Composable
    override fun ScreenContent() {
        val menuItems = listOf(
            BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 3)),
            BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 3)),
            BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 6)),
            BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 3, true)),
            BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 3)),
            BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 6)),
            BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 4, false)),
            BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 2)),
            BaseCard(TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 6)),
        )

        StaticFlexibleGrid(items = menuItems)
    }

}