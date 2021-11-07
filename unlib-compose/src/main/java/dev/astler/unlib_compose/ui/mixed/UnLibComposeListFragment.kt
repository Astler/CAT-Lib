package dev.astler.unlib_compose.ui.mixed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.unlib_compose.R

@AndroidEntryPoint
abstract class UnLibComposeListFragment<T> : UnLibComposeCoreFragment() {
    @Composable
    open fun ItemsList(modifier: Modifier = Modifier, contentPadding: PaddingValues = PaddingValues(0.dp), pData: Array<T>?) {

        if (pData == null) {
            LoadingScreen()
            return
        }

        if (pData.isEmpty()) {
            EmptyScreen()
            return
        }

        LazyColumn(modifier, contentPadding = contentPadding) {
            items(pData) { pItem ->
                ContentItem(pItem)
            }
        }
    }

    @Composable
    open fun ContentItem(pItem: T) {
    }

    @Composable
    open fun LoadingScreen() {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Loading")
                CircularProgressIndicator()
            }
        }
    }

    @Composable
    open fun EmptyScreen() {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.nothing_to_show))
            }
        }
    }
}
