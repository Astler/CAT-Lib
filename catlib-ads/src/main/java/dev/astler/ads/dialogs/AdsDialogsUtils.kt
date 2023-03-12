package dev.astler.ads.dialogs

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import dev.astler.ads.interfaces.IAdListener
import dev.astler.cat_ui.utils.dialogs.confirmDialog
import dev.astler.catlib.gAppConfig
import dev.astler.catlib.utils.openAppInPlayStore
import dev.astler.catlib.ads.R

fun AppCompatActivity.showNoAdsDialog() {
    val proPackageName = gAppConfig.mProPackageName

    if (proPackageName.isNotEmpty()) {
        confirmDialog(
            dev.astler.catlib.core.R.string.disable_ads,
            dev.astler.catlib.core.R.string.disable_ads_msg,
            positive = dev.astler.catlib.core.R.string.buy_pro,
            negative = dev.astler.catlib.core.R.string.watch_ads,
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
            dev.astler.catlib.core.R.string.disable_ads,
            dev.astler.catlib.core.R.string.disable_ads_msg,
            negative = android.R.string.cancel,
            positive = dev.astler.catlib.core.R.string.watch_ads,
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
        positive = dev.astler.catlib.core.R.string.yes,
        negative = dev.astler.catlib.core.R.string.no,
        positiveAction = {
            result.invoke(false)
        },
        negativeAction = {
            result.invoke(true)
        }
    )
}