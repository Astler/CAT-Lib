package dev.astler.ads.dialogs

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import dev.astler.ads.interfaces.IAdListener
import dev.astler.cat_ui.utils.dialogs.confirmDialog
import dev.astler.catlib.ads.R
import dev.astler.catlib.gAppConfig
import dev.astler.catlib.utils.openAppInPlayStore

fun AppCompatActivity.showNoAdsDialog() {
    val proPackageName = gAppConfig.mProPackageName

    if (proPackageName.isNotEmpty()) {
        confirmDialog(
            R.string.disable_ads,
            R.string.disable_ads_msg,
            positive = R.string.buy_pro,
            negative = R.string.watch_ads,
            positiveAction = {
                openAppInPlayStore(proPackageName)
            },
            negativeAction = {
                if (this is IAdListener)
                    showRewardAd()
            }
        )
    } else {
        confirmDialog(
            R.string.disable_ads,
            R.string.disable_ads_msg,
            negative = android.R.string.cancel,
            positive = R.string.watch_ads,
            positiveAction = {
                if (this is IAdListener)
                    showRewardAd()
            },
            negativeAction = {}
        )
    }
}

fun Context.adsAgeConfirmDialog(result: (Boolean) -> Unit) {
    confirmDialog(
        R.string.ads_dialog_title,
        R.string.ads_dialog_msg,
        positive = R.string.yes,
        negative = R.string.no,
        positiveAction = {
            result.invoke(false)
        },
        negativeAction = {
            result.invoke(true)
        }
    )
}