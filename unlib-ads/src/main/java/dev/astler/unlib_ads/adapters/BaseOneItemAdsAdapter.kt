package dev.astler.unlib_ads.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import dev.astler.unlib.adapters.viewholders.BaseOneItemListViewHolder
import dev.astler.unlib.interfaces.RecyclerAdapterSizeListener
import dev.astler.unlib.utils.*
import dev.astler.unlib_ads.R
import dev.astler.unlib_ads.adapters.viewholders.AdItemViewHolder
import dev.astler.unlib_ads.utils.NativeAdsLoader
import kotlin.random.Random

open class BaseOneItemAdsAdapter<T>(
    context: Context,
    @LayoutRes val pLayoutResource: Int,
    private val mItemLoadListener: LoadItem<T>? = null,
    private val mAdapterSizeListener: RecyclerAdapterSizeListener? = null,
    private val mFirstAdPosition: Int = 2,
    private val mAdDelta: Int = 5
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val adItem = 999

    private var canShowAds = context.canShowAds()
    var data: ArrayList<T> = arrayListOf()

    fun addItems(items: List<T>) {
        this.data.clear()
        this.data.addAll(items)
        mAdapterSizeListener?.totalItems(this.data.size)
        notifyDataSetChanged()
    }

    fun silenceReloadItems(items: List<T>) {
        this.data.clear()
        this.data.addAll(items)
        mAdapterSizeListener?.totalItems(this.data.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            adItem -> AdItemViewHolder(parent.initView(R.layout.item_ad))
            else -> {
                val nHolderItem =
                    LayoutInflater.from(parent.context).inflate(pLayoutResource, parent, false)
                BaseOneItemListViewHolder(nHolderItem)
            }
        }
    }

    private fun getAdForPosition(): NativeAd? {
        return NativeAdsLoader.instance?.getNativeAd(
            Random.nextInt(NativeAdsLoader.NUMBER_OF_ADS)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AdItemViewHolder) {
            val adForPosition = getAdForPosition()
            val adView: NativeAdView = holder.adView

            if (adForPosition == null || !adView.context.canShowAds()) {
                infoLog("HIDE AD >.<")
                holder.mItemAdBinding.adHeadline.goneView()
                holder.mItemAdBinding.nativeAd.goneView()
            } else {
                infoLog("SHOW AD >.<")
                holder.mItemAdBinding.adHeadline.showView()
                holder.mItemAdBinding.nativeAd.showView()

                (adView.headlineView as TextView).text = adForPosition.headline
                (adView.bodyView as TextView).text = adForPosition.body

                if (adForPosition.icon == null) {
                    adView.iconView?.goneView()
                } else {
                    (adView.iconView as ImageView).setImageDrawable(adForPosition.icon?.drawable)
                    adView.iconView?.showView()
                }

                if (adForPosition.callToAction != null) {
                    adView.callToActionView?.showView()
                    (adView.callToActionView as TextView).text = adForPosition.callToAction
                } else {
                    adView.callToActionView?.goneView()
                }

                adView.setNativeAd(adForPosition)
            }
        } else {
            if (holder is BaseOneItemListViewHolder)
                mItemLoadListener?.loadData(data[getRealPosition(position)], holder)
        }
    }

    private fun getRealPosition(position: Int): Int {
        return position -
            if (position > mFirstAdPosition && mAdDelta != 0) {
                var minus = 0

                if (position > mFirstAdPosition) {
                    minus++

                    if (position - mFirstAdPosition >= mAdDelta) {
                        minus += (position - mFirstAdPosition) / (mAdDelta)
                    }
                }

                minus
            } else 0
    }

    override fun getItemCount(): Int {
        mAdapterSizeListener?.totalItems(data.size)

        if (mAdDelta != 0 && canShowAds) {
            var additionalContent = 0

            if (data.size > 0 && mFirstAdPosition > 0 && data.size > mFirstAdPosition)
                additionalContent++

            if ((data.size - mFirstAdPosition) > 0 && mAdDelta > 0 && (data.size - mFirstAdPosition) > mAdDelta) {
                additionalContent += (data.size - mFirstAdPosition) / mAdDelta
            }

            var size = data.size + additionalContent

            var recheckContentAdd = 0

            while (additionalContent != recheckContentAdd) {
                if (size > 0 && mFirstAdPosition > 0 && size > mFirstAdPosition)
                    recheckContentAdd++

                if ((size - mFirstAdPosition) > 0 && mAdDelta > 0 && (size - mFirstAdPosition) > mAdDelta) {
                    recheckContentAdd += (size - mFirstAdPosition) / mAdDelta
                }

                size = data.size + recheckContentAdd

                if (additionalContent != recheckContentAdd) {
                    additionalContent = recheckContentAdd
                    recheckContentAdd = 0
                }
            }

            return size
        } else {
            return data.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position > 0 && mAdDelta != 0 &&
            (position == mFirstAdPosition || (position - mFirstAdPosition) % mAdDelta == 0) &&
            canShowAds
        ) {
            return adItem
        } else 0
    }

    fun interface LoadItem<T> {
        fun loadData(pData: T, pHolder: BaseOneItemListViewHolder)
    }
}
