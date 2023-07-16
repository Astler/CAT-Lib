package dev.astler.unlib_test.fragments.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.astler.unlib_compose.ui.compose.flexible_grid.FlexibleGrid
import dev.astler.unlib_compose.ui.mixed.CatComposeFragment
import dev.astler.unlib_test.R
import dev.astler.unlib_test.data.TestBaseItem

abstract class TestsMenuFragment : CatComposeFragment() {

    open val menuItems = listOf(
        TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 2),
        TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 2),
        TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 2),
        TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 3, true),
        TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 3),
        TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 6),
        TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 4, false),
        TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 2),
        TestBaseItem(R.string.app_name, R.drawable.ic_launcher_foreground, 6),
    )

    open fun menuItemClicked(uid: String) {

    }

    @Composable
    override fun ScreenContent() {
        FlexibleGrid(menuItems, onItemClick = ::menuItemClicked)
    }
}