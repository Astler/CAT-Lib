package dev.astler.unlib_compose.ui.mixed

import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.unlib_compose.screens.apps.AppItem
import dev.astler.unlib_compose.screens.apps.nAppsData

@AndroidEntryPoint
class MyAppsComposeFragment : ListComposeFragment<AppItem>() {

    @Composable
    override fun ScreenContent() {
        ItemsList(pData = nAppsData)
    }

    @Composable
    override fun ContentItem(pItem: AppItem) {
        AppItem(pItem)
    }

    override fun onFragmentBackPressed(endAction: () -> Unit) {
    }
}
