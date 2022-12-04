package dev.astler.ads.adapters.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import dev.astler.catlib.utils.infoLog
import dev.astler.cat_ui.utils.views.goneView
import dev.astler.cat_ui.utils.views.showView
import dev.astler.catlib.ads.databinding.ItemAdBinding

class AdItemViewHolder(view: View) :
    ViewHolder(view) {
    val mItemAdBinding = ItemAdBinding.bind(view)
    val adView: NativeAdView
    private val mView: View

    init {
        val unifiedNativeAdView = mItemAdBinding.nativeAd
        adView = unifiedNativeAdView
        mView = view
        adView.callToActionView = mItemAdBinding.install
        adView.headlineView = mItemAdBinding.adHeadline
        adView.bodyView = mItemAdBinding.adBody
        adView.iconView = mItemAdBinding.adAppIcon
    }

    fun initNativeBanner(pNativeAd: NativeAd?, pCanShowAds: Boolean) {
        val adView: NativeAdView = adView

        infoLog("AD >.< \n mCanShowAds = $pCanShowAds \n adForPosition = $pNativeAd", "Ads")

        if (pNativeAd == null || !pCanShowAds) {
            infoLog("HIDE AD >.<", "Ads")
            mItemAdBinding.adHeadline.goneView()
            mItemAdBinding.nativeAd.goneView()
        } else {
            infoLog("SHOW AD >.<", "Ads")
            mItemAdBinding.adHeadline.showView()
            mItemAdBinding.nativeAd.showView()

            (adView.headlineView as TextView).text = pNativeAd.headline
            (adView.bodyView as TextView).text = pNativeAd.body

            if (pNativeAd.icon == null) {
                adView.iconView?.goneView()
                mItemAdBinding.adAppIconCard.goneView()
            } else {
                (adView.iconView as ImageView).setImageDrawable(pNativeAd.icon?.drawable)
                adView.iconView?.showView()
                mItemAdBinding.adAppIconCard.showView()
            }

            if (pNativeAd.callToAction != null) {
                adView.callToActionView?.showView()
                (adView.callToActionView as TextView).text = pNativeAd.callToAction
            } else {
                adView.callToActionView?.goneView()
            }

            adView.setNativeAd(pNativeAd)
        }
    }
}
