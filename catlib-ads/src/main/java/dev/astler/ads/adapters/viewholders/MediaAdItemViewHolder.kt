package dev.astler.ads.adapters.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import dev.astler.ui.utils.views.gone
import dev.astler.ui.utils.views.visible
import dev.astler.ui.utils.views.showWithCondition
import dev.astler.catlib.helpers.adsLog
import dev.astler.catlib.ads.databinding.ItemMediaAdBinding

class MediaAdItemViewHolder(view: View) : ViewHolder(view) {

    private val _itemBinding = ItemMediaAdBinding.bind(view)
    private val _adView: NativeAdView = _itemBinding.nativeAd
    private val _view: View

    init {
        _view = view

        _adView.callToActionView = _itemBinding.install
        _adView.headlineView = _itemBinding.adHeadline
        _adView.bodyView = _itemBinding.adBody
        _adView.mediaView = _itemBinding.adAppIcon
    }

    fun initNativeMediaBanner(pNativeAd: NativeAd?, pCanShowAds: Boolean) {

        with(_itemBinding) {
            val canLoadAds = pNativeAd != null && pCanShowAds

            adHeadline.showWithCondition(canLoadAds)
            nativeAd.showWithCondition(canLoadAds)

            if (!canLoadAds) {
                adsLog("Hide ad: data = $pNativeAd and canShowAds = $pCanShowAds")
                return
            }

            if (pNativeAd == null) return

            adsLog("Show ads")

            (_adView.headlineView as TextView).text = pNativeAd.headline
            (_adView.bodyView as TextView).text = pNativeAd.body

            if (pNativeAd.callToAction != null) {
                _adView.callToActionView?.visible()
                (_adView.callToActionView as TextView).text = pNativeAd.callToAction
            } else {
                _adView.callToActionView?.gone()
            }

            _adView.setNativeAd(pNativeAd)
        }
    }
}
