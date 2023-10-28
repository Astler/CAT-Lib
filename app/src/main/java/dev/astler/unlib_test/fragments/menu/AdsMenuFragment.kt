package dev.astler.unlib_test.fragments.menu

import dagger.hilt.android.AndroidEntryPoint
import dev.astler.ads.utils.canShowAds
import dev.astler.cat_ui.utils.dialogs.okDialog
import dev.astler.unlib_compose.ui.compose.items.BaseCard
import dev.astler.unlib_test.R
import dev.astler.unlib_test.data.TestBaseItem

@AndroidEntryPoint
class AdsMenuFragment: TestsMenuFragment() {

    private val adsInfoKey = "AdsInfo"

    override val menuItems = listOf(
        BaseCard(TestBaseItem(R.string.ads_info, R.drawable.ic_launcher_foreground, 3, uid = adsInfoKey)),
    )

    override fun menuItemClicked(uid: String) {
        when (uid) {
            adsInfoKey -> {
                safeContext.okDialog(title = "Ads Info", message = "Can show AD: ${safeContext.canShowAds(preferences)}")
            }

        }
    }
}