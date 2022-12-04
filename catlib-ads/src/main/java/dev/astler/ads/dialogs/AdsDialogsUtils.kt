package dev.astler.ads.dialogs

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import dev.astler.cat_ui.utils.dialogs.confirmDialog
import dev.astler.catlib.ads.R
import dev.astler.catlib.gPreferencesTool
import dev.astler.catlib.utils.openAppInPlayStore
import dev.astler.ads.utils.mProPackageName
import dev.astler.ads.utils.showRewardAd

fun AppCompatActivity.showNoAdsDialog() {
    if (mProPackageName.isNotEmpty()) {
        confirmDialog(
            R.string.disable_ads,
            R.string.disable_ads_msg,
            R.string.buy_pro,
            R.string.watch_ads,
            positiveAction = {
                openAppInPlayStore(mProPackageName)
            },
            negativeAction = { showRewardAd() }
        )
    } else {
        confirmDialog(
            R.string.disable_ads,
            R.string.disable_ads_msg,
            R.string.watch_ads,
            positiveAction = {
                showRewardAd()
            }
        )
    }
}

fun Context.adsAgeDialog() {
    confirmDialog(
        R.string.ads_dialog_title,
        R.string.ads_dialog_msg,
        R.string.yes,
        R.string.no,
        positiveAction = {
            gPreferencesTool.edit("child_ads", false)
        },
        negativeAction = {
            gPreferencesTool.edit("child_ads", true)
        }
    )
}