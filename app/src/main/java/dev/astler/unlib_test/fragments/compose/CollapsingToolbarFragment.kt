package dev.astler.unlib_test.fragments.compose

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import com.ao.subscribeme.R
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.unlib_compose.ui.compose.collapse_toolbar.CollapsingToolbarContent
import dev.astler.unlib_compose.ui.compose.flexible_grid.FlexibleGrid
import dev.astler.unlib_compose.ui.compose.items.BaseCard
import dev.astler.unlib_compose.ui.mixed.CatComposeFragment
import dev.astler.unlib_test.data.TestBaseItem

@AndroidEntryPoint
class CollapsingToolbarFragment : CatComposeFragment() {

    @Composable
    override fun ScreenContent() {
        val gridState = rememberLazyGridState()

        val menuItems = listOf(
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

        CollapsingToolbarContent(topReached = {
            gridState.firstVisibleItemIndex == 0 && gridState.firstVisibleItemScrollOffset == 0
        }) {
            FlexibleGrid(modifier = it, state = gridState, items = menuItems)
        }
    }

}