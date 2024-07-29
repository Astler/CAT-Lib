package dev.astler.unlib_test.fragments.compose

import androidx.compose.runtime.Composable
import com.ao.subscribeme.R
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.ui.compose.flexible_grid.StaticFlexibleGrid
import dev.astler.ui.compose.items.BaseCard
import dev.astler.ui.fragments.CatComposeFragment
import dev.astler.unlib_test.data.TestBaseItem

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