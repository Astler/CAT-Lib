package dev.astler.unlib_compose.screens.apps

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.astler.catlib.compose.R

data class AppItem(
    val packageName: String,
    val appNameId: Int = -1,
    val appDescriptionId: Int = -1,
    val appIcon: Int
)

//TODO finish data block
val nAppsData = arrayOf(
    AppItem(
        "be",
        gg.pressf.resources.R.string.nothing,
        gg.pressf.resources.R.string.nothing,
        R.drawable.banner
    ),
    AppItem(
        "gtb",
        gg.pressf.resources.R.string.nothing,
        gg.pressf.resources.R.string.nothing,
        R.drawable.gtb
    ),
    AppItem(
        "kb",
        gg.pressf.resources.R.string.nothing,
        gg.pressf.resources.R.string.nothing,
        R.drawable.kb
    ),
)

@Composable
fun AppItem(appData: AppItem, onClicked: (String) -> Unit) {
    Row(
        Modifier
            .padding(8.dp)
            .clickable {
                onClicked(appData.packageName)
            }
    ) {
        Image(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(8)),
            painter = painterResource(appData.appIcon),
            contentDescription = stringResource(appData.appNameId),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = stringResource(appData.appNameId),
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.onBackground
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = stringResource(appData.appDescriptionId),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onBackground
            )
        }
    }
}

@Preview
@Composable
fun AppItemPreview() {
    AppItem(nAppsData[0]) {

    }
}