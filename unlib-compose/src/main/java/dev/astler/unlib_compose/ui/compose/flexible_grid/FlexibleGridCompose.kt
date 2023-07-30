package dev.astler.unlib_compose.ui.compose.flexible_grid

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.astler.unlib_compose.data.IFlexibleItem
import dev.astler.unlib_compose.interfaces.IComposeItem

@Composable
fun <T: IFlexibleItem, B> FlexibleGrid(
    modifier: Modifier = Modifier,
    items: List<IComposeItem<T, B>>,
    cellsCount: Int = 6,
    padding: Dp = 8.dp,
    state: LazyGridState? = null,
    onItemClick: ((B) -> Unit)? = null
) {
    LazyVerticalGrid(
        state = state ?: rememberLazyGridState(),
        modifier = modifier,
        columns = GridCells.Fixed(cellsCount),
        contentPadding = PaddingValues(padding)
    ) {
        items.forEach { card ->
            item(span = { GridItemSpan(card.item.size) }) {
                card.Content(Modifier, onClick = onItemClick)
            }
        }
    }
}