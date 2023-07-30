package dev.astler.unlib_compose.ui.compose.flexible_grid

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.astler.unlib_compose.data.IFlexibleItem
import dev.astler.unlib_compose.interfaces.IComposeItem

@Composable
fun <T : IFlexibleItem, B> StaticFlexibleGrid(
    modifier: Modifier = Modifier,
    items: List<IComposeItem<T, B>>,
    cellsCount: Int = 6,
    padding: Dp = 8.dp,
    onItemClick: ((B) -> Unit)? = null
) {
    val chunkedItems = items.chunkedByWeight(cellsCount) { it.item.size }

    Column(
        modifier = modifier.padding(padding)
    ) {
        chunkedItems.forEach { rowItems ->
            Row(Modifier.fillMaxWidth()) {
                rowItems.forEach { item ->
                    Box(
                        Modifier
                            .weight((item.item.size).toFloat()).height(IntrinsicSize.Min)
                            .align(Alignment.Top)
                    ) {
                        item.Content(Modifier, onClick = onItemClick)
                    }
                }
            }
        }
    }
}

private inline fun <T> List<T>.chunkedByWeight(
    weight: Int, crossinline weightSelector: (T) -> Int
): List<List<T>> {
    val result = mutableListOf<List<T>>()
    var currentChunk = mutableListOf<T>()
    var currentWeight = 0

    for (item in this) {
        val itemWeight = weightSelector(item)

        if (currentWeight + itemWeight <= weight) {
            currentChunk.add(item)
            currentWeight += itemWeight
        } else {
            result.add(currentChunk)
            currentChunk = mutableListOf(item)
            currentWeight = itemWeight
        }
    }

    if (currentChunk.isNotEmpty()) {
        result.add(currentChunk)
    }

    return result
}
