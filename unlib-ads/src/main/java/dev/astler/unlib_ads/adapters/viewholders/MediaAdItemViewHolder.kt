package dev.astler.unlib_ads.adapters.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import dev.astler.unlib.utils.goneView
import dev.astler.unlib.utils.infoLog
import dev.astler.unlib.utils.showView
import dev.astler.unlib_ads.databinding.ItemMediaAdBinding

class MediaAdItemViewHolder(view: View) :
    ViewHolder(view) {
    private val mItemAdBinding = ItemMediaAdBinding.bind(view)
    private val adView: NativeAdView
    private val mView: View

    init {
        val unifiedNativeAdView = mItemAdBinding.nativeAd
        adView = unifiedNativeAdView
        mView = view
        adView.callToActionView = mItemAdBinding.install
        adView.headlineView = mItemAdBinding.adHeadline
        adView.bodyView = mItemAdBinding.adBody
        adView.mediaView = mItemAdBinding.adAppIcon
    }

    fun initNativeMediaBanner(pNativeAd: NativeAd?, pCanShowAds: Boolean) {
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
