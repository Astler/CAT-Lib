package dev.astler.unlib_compose.ui.compose.items

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.astler.catlib.compose.R
import dev.astler.unlib_compose.data.IFlexibleItem
import dev.astler.unlib_compose.interfaces.IComposeItem

@OptIn(ExperimentalMaterial3Api::class)
class BaseCard(override val item: IFlexibleItem) : IComposeItem<IFlexibleItem, String> {

    @Composable
    override fun Content(modifier: Modifier, onClick: ((String) -> Unit)?) {
        val colors = MaterialTheme.colorScheme

        OutlinedCard(
            modifier = modifier
                .padding(4.dp)
                .wrapContentHeight(),
            border = BorderStroke(1.dp, colors.outlineVariant),
            onClick = { onClick?.invoke(item.uid) },
        ) {
            Box(
                modifier = Modifier.wrapContentHeight(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item.iconId?.let { iconId ->
                        Icon(
                            painter = painterResource(iconId),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
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
                                style = typography.h6
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
                                    style = typography.h6.copy(
                                        shadow = Shadow(
                                            color = colors.onSurface.copy(alpha = 0.5f),
                                            offset = Offset(4f, 4f),
                                            blurRadius = 2f
                                        )
                                    )
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}
