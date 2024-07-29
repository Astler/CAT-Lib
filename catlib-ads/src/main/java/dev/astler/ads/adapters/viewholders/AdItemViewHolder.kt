package dev.astler.ads.adapters.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import dev.astler.catlib.helpers.infoLog
import dev.astler.ui.utils.views.gone
import dev.astler.ui.utils.views.visible
import dev.astler.catlib.ads.databinding.ItemAdBinding

class AdItemViewHolder(view: View) :
    ViewHolder(view) {
    private val itemBinding = ItemAdBinding.bind(view)
    private val adView: NativeAdView = itemBinding.nativeAd
    private val _view: View

    init {
        _view = view
        adView.callToActionView = itemBinding.install
        adView.headlineView = itemBinding.adHeadline
        adView.bodyView = itemBinding.adBody
        adView.iconView = itemBinding.adAppIcon
    }

    fun initNativeBanner(pNativeAd: NativeAd?, pCanShowAds: Boolean) {
        val adView: NativeAdView = adView

        infoLog("AD >.< \n mCanShowAds = $pCanShowAds \n adForPosition = $pNativeAd", "Ads")

        if (pNativeAd == null || !pCanShowAds) {
            infoLog("HIDE AD >.<", "Ads")
            itemBinding.adHeadline.gone()
            itemBinding.nativeAd.gone()
        } else {
            infoLog("SHOW AD >.<", "Ads")
            itemBinding.adHeadline.visible()
            itemBinding.nativeAd.visible()

            (adView.headlineView as TextView).text = pNativeAd.headline
            (adView.bodyView as TextView).text = pNativeAd.body

            if (pNativeAd.icon == null) {
                adView.iconView?.gone()
                itemBinding.adAppIconCard.gone()
            } else {
                (adView.iconView as ImageView).setImageDrawable(pNativeAd.icon?.drawable)
                adView.iconView?.visible()
                itemBinding.adAppIconCard.visible()
            }

            if (pNativeAd.callToAction != null) {
                adView.callToActionView?.visible()
                (adView.callToActionView as TextView).text = pNativeAd.callToAction
            } else {
                adView.callToActionView?.gone()
            }

            adView.setNativeAd(pNativeAd)
        }
    }
}
