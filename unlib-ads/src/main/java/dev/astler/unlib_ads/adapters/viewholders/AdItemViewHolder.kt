package dev.astler.unlib_ads.adapters.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.gms.ads.nativead.NativeAdView
import dev.astler.unlib_ads.databinding.ItemAdBinding

class AdItemViewHolder(view: View) :
    ViewHolder(view) {
    val mItemAdBinding = ItemAdBinding.bind(view)
    val adView: NativeAdView
    val mView: View

    init {
        val unifiedNativeAdView = mItemAdBinding.nativeAd
        adView = unifiedNativeAdView
        mView = view
        adView.callToActionView = mItemAdBinding.install
     //   adView.headlineView = mItemAdBinding.adHeadline
        adView.bodyView = mItemAdBinding.adBody
        adView.iconView = mItemAdBinding.adAppIcon
    }
}
