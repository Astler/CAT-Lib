package dev.astler.unlib_compose.ui.mixed

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class UnLibComposeListFragment<T> : UnLibComposeCoreFragment() {
    @Composable
    open fun ItemsList(pData: Array<T>) {
        LazyColumn {
            items(pData) { pItem ->
                ContentItem(pItem)
            }
        }
    }

    @Composable
    open fun ContentItem(pItem: T) {}
}
