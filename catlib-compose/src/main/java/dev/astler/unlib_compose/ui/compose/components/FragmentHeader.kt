package dev.astler.unlib_compose.ui.compose.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.astler.unlib_compose.extensions.isScreenLandscape
import dev.astler.unlib_compose.extensions.thenIf

@Composable
fun FragmentHeader(
    textSize: Float,
    @DrawableRes drawableRes: Int,
    @StringRes titleRes: Int,
    iconPadding: PaddingValues = PaddingValues(16.dp),
    description: String? = null,
    roundPicture: Boolean = false
) {
    val isLandscape = isScreenLandscape()

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(
            modifier = Modifier
                .imePadding()
        )

        Spacer(
            modifier = Modifier.size(if (isLandscape) 24.dp else 36.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            AsyncImage(
                model = drawableRes,
                modifier = Modifier
                    .size(if (isLandscape) 56.dp else 78.dp)
                    .padding(iconPadding).thenIf(roundPicture, Modifier.clip(RoundedCornerShape(8.dp))),
                contentDescription = null,
                alignment = Alignment.CenterEnd
            )

            Spacer(modifier = Modifier.size(8.dp))

            Column(
                modifier = Modifier.widthIn(max = 300.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                EnchantedText(
                    modifier = Modifier.wrapContentSize(),
                    text = stringResource(id = titleRes),
                    isBold = true,
                    textSize = textSize + 2,
                    centered = true
                )

                description?.let {
                    EnchantedText(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(top = 8.dp),
                        text = description,
                        textSize = textSize - 4,
                        centered = true
                    )
                }

            }
        }
    }
}