package dev.astler.unlib_test.fragments.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.catlib.signin.SignInTool
import dev.astler.unlib_compose.data.IFlexibleItem
import dev.astler.unlib_compose.interfaces.IComposeItem
import dev.astler.unlib_compose.ui.compose.flexible_grid.FlexibleGrid
import dev.astler.unlib_compose.ui.compose.items.BaseCard
import dev.astler.unlib_compose.ui.mixed.CatComposeFragment
import com.ao.subscribeme.R
import dev.astler.unlib_test.data.TestBaseItem
import javax.inject.Inject

@AndroidEntryPoint
abstract class TestsMenuFragment : CatComposeFragment() {

    @Inject
    lateinit var singInTool: SignInTool

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
        Column {
            if (singInTool.isSignedIn) {
                Text(text = "Signed in")
            } else {
                Text(text = "Not signed in")
            }

            FlexibleGrid(items = menuItems, onItemClick = ::menuItemClicked)
        }
    }
}