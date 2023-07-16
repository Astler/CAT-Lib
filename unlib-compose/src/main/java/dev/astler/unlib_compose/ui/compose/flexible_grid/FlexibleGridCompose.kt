package dev.astler.unlib_compose.ui.compose.flexible_grid

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.astler.unlib_compose.data.IFlexibleItem

@Composable
fun FlexibleGrid(
    items: List<IFlexibleItem>,
    cellsCount: Int = 6,
    padding: Dp = 8.dp,
    onItemClick: ((String) -> Unit)? = null,
    flexibleItem: ((IFlexibleItem) -> Unit)? = null
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(cellsCount),
//            columns = object : GridCells {
//                override fun Density.calculateCrossAxisCellSizes(
//                    availableSize: Int,
//                    spacing: Int
//                ): List<Int> {
//                    val firstColumn = (availableSize - spacing) * 2 / 3
//                    val secondColumn = availableSize - spacing - firstColumn
//                    return listOf(firstColumn, secondColumn)
//                }
//            },
        contentPadding = PaddingValues(padding)
    ) {
        items.forEach { itemData ->
            item(span = { GridItemSpan(itemData.size) }) {
                if (flexibleItem != null) {
                    flexibleItem.invoke(itemData)
                } else {
                    FlexibleGridItem(itemData, onItemClick)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlexibleGridItem(item: IFlexibleItem, onItemClick: ((String) -> Unit)? = null) {
    val colors = MaterialTheme.colorScheme

    OutlinedCard(
        modifier = Modifier.padding(8.dp),
        border = BorderStroke(1.dp, colors.onSurface),
        onClick = { onItemClick?.invoke(item.uid) },
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .background(colors.surface, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (item.iconId != null) {
                    Icon(
                        painter = painterResource(item.iconId!!),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = colors.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (item.titleId != null) {
                    Text(
                        text = stringResource(item.titleId!!),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface,
                        maxLines = 2,
                        minLines = 2
                    )
                }
            }
        }
    }
}