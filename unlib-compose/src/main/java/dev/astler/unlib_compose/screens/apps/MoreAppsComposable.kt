package dev.astler.unlib_compose.screens.apps

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.* // ktlint-disable no-wildcard-imports
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.astler.catlib.utils.openAppInPlayStore
import dev.astler.unlib_compose.R

data class AppItem(
    val pAppId: String,
    val pAppNameRes: Int = -1,
    val pAppDescriptionRes: Int = -1,
    val pAppIconRes: Int
)

val nAppsData = arrayOf(
    AppItem("be", R.string.app_name, R.string.app_name, R.drawable.banner),
    AppItem("gtb", R.string.app_name, R.string.app_name, R.drawable.gtb),
    AppItem("kb", R.string.app_name, R.string.app_name, R.drawable.kb),
)

@Composable
fun AppItem(pAppData: AppItem) {
    val nLocalContext = LocalContext.current
    Row(
        Modifier
            .padding(8.dp)
            .clickable {
                nLocalContext.openAppInPlayStore(pAppData.pAppId)
            }
    ) {
        Image(
            modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8)),
            painter = painterResource(pAppData.pAppIconRes),
            contentDescription = stringResource(pAppData.pAppNameRes),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = stringResource(pAppData.pAppNameRes),
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.onBackground
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = stringResource(pAppData.pAppDescriptionRes),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onBackground
            )
        }
    }
}
