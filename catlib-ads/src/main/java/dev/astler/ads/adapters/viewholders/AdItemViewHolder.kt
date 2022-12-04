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
            itemBinding.adHeadline.goneView()
            itemBinding.nativeAd.goneView()
        } else {
            infoLog("SHOW AD >.<", "Ads")
            itemBinding.adHeadline.showView()
            itemBinding.nativeAd.showView()

            (adView.headlineView as TextView).text = pNativeAd.headline
            (adView.bodyView as TextView).text = pNativeAd.body

            if (pNativeAd.icon == null) {
                adView.iconView?.goneView()
                itemBinding.adAppIconCard.goneView()
            } else {
                (adView.iconView as ImageView).setImageDrawable(pNativeAd.icon?.drawable)
                adView.iconView?.showView()
                itemBinding.adAppIconCard.showView()
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
