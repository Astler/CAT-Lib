package dev.astler.unlib_ads.utils.dialogs

import androidx.appcompat.app.AppCompatActivity
import dev.astler.cat_ui.utils.dialogs.confirmDialog
import dev.astler.unlib.utils.openAppInPlayStore
import dev.astler.unlib_ads.R
import dev.astler.unlib_ads.utils.mProPackageName
import dev.astler.unlib_ads.utils.showRewardAd

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