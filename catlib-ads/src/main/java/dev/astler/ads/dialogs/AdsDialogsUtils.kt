package dev.astler.ads.dialogs

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import dev.astler.ads.interfaces.IAdToolProvider
import dev.astler.ui.utils.dialogs.unpackedCatDialog
import dev.astler.catlib.extensions.openAppInPlayStore
import dev.astler.catlib.ads.R

fun AppCompatActivity.showNoAdsDialog(proPackageName: String) {
    if (proPackageName.isNotEmpty()) {
        unpackedCatDialog(
            gg.pressf.resources.R.string.disable_ads,
            gg.pressf.resources.R.string.disable_ads_msg,
            positive = gg.pressf.resources.R.string.buy_pro,
            negative = gg.pressf.resources.R.string.watch_ads,
            positiveAction = {
                openAppInPlayStore(proPackageName)
            },
            negativeAction = {
                if (this is IAdToolProvider)
                    showRewardAd()
            }
        )
    } else {
        unpackedCatDialog(
            gg.pressf.resources.R.string.disable_ads,
            gg.pressf.resources.R.string.disable_ads_msg,
            negative = android.R.string.cancel,
            positive = gg.pressf.resources.R.string.watch_ads,
            positiveAction = {
                if (this is IAdToolProvider)
                    showRewardAd()
            },
            negativeAction = {}
        )
    }
}

fun Context.adsAgeConfirmDialog(result: (Boolean) -> Unit) {
    unpackedCatDialog(
        R.string.ads_dialog_title,
        R.string.ads_dialog_msg,
        positive = gg.pressf.resources.R.string.yes,
        negative = gg.pressf.resources.R.string.no,
        positiveAction = {
            result.invoke(false)
        },
        negativeAction = {
            result.invoke(true)
        }
    )
}