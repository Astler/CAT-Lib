package dev.astler.ui.compose.items

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.astler.ui.data.IFlexibleItem
import dev.astler.ui.interfaces.IComposeItem

@OptIn(ExperimentalMaterial3Api::class)
class ElevatedBaseCard(override val item: IFlexibleItem) : IComposeItem<IFlexibleItem, String> {

    @Composable
    override fun Content(modifier: Modifier, onClick: ((String) -> Unit)?) {
        val colors = MaterialTheme.colorScheme

        ElevatedCard(
            modifier = Modifier.padding(4.dp),
            onClick = { onClick?.invoke(item.uid) },
        ) {
            Box(contentAlignment = Alignment.Center) {
                Column(
                    modifier = Modifier.wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item.iconId?.let { iconId ->
                        val transition = rememberInfiniteTransition(label = "")
                        val scale by transition.animateFloat(
                            initialValue = 1f,
                            targetValue = 1.2f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(500, easing = FastOutSlowInEasing),
                                repeatMode = RepeatMode.Reverse
                            ), label = ""
                        )

                        Icon(
                            painter = painterResource(iconId),
                            contentDescription = null,
                            modifier = Modifier.scale(scale).fillMaxWidth().height(56.dp)
                                .absolutePadding(left = 4.dp, right = 4.dp, top = 8.dp),
                            tint = colors.onSurface
                        )
                    }

                    item.titleId?.let { titleId ->
                        val fullText = stringResource(titleId)

                        Box(
                            modifier = Modifier
                                .fillMaxSize().height(IntrinsicSize.Min)
                                .absolutePadding(left = 8.dp, right = 8.dp, bottom = 8.dp, top = 2.dp)
                        ) {
                            Text(
                                text = "\n\n",
                                color = Color.Transparent,
                                maxLines = 2,
                                style = androidx.compose.material.MaterialTheme.typography.h6
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    text = fullText,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.onSurface,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    style = androidx.compose.material.MaterialTheme.typography.h6
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
