package dev.astler.unlib_ads.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.nativead.NativeAd
import dev.astler.cat_ui.adapters.viewholders.CatOneTypeViewHolder
import dev.astler.cat_ui.interfaces.RecyclerAdapterSizeListener
import dev.astler.cat_ui.utils.views.inflateById
import dev.astler.unlib_ads.R
import dev.astler.unlib_ads.adapters.diffutils.OneItemDiffUtil
import dev.astler.unlib_ads.adapters.viewholders.AdItemViewHolder
import dev.astler.unlib_ads.adapters.viewholders.MediaAdItemViewHolder
import dev.astler.unlib_ads.utils.NativeAdsLoader
import kotlin.random.Random

data class OIAdsAdapterConfig(
    var mCanShowAds: Boolean = false,
    var mUseMediaBanner: Boolean = false,
    val mFirstAdPosition: Int = 2,
    val mAdDelta: Int = 5
) {
    fun canShowAds(): Boolean {
        return mAdDelta != 0 && mCanShowAds
    }

    fun canPlaceAd(pPosition: Int): Boolean {
        return pPosition > 0 &&
            (pPosition == mFirstAdPosition || (pPosition - mFirstAdPosition) % mAdDelta == 0) && canShowAds()
    }

    fun calcRecyclerSize(pDataSize: Int): Int {
        if (mCanShowAds) {
            var additionalContent = 0

            if (pDataSize != 0 && mFirstAdPosition > 0 && pDataSize > mFirstAdPosition)
                additionalContent++

            if ((pDataSize - mFirstAdPosition) > 0 && mAdDelta > 0 && (pDataSize - mFirstAdPosition) > mAdDelta) {
                additionalContent += (pDataSize - mFirstAdPosition) / mAdDelta
            }

            var size = pDataSize + additionalContent

            var recheckContentAdd = 0

            while (additionalContent != recheckContentAdd) {
                if (size > 0 && mFirstAdPosition > 0 && size > mFirstAdPosition)
                    recheckContentAdd++

                if ((size - mFirstAdPosition) > 0 && mAdDelta > 0 && (size - mFirstAdPosition) > mAdDelta) {
                    recheckContentAdd += (size - mFirstAdPosition) / mAdDelta
                }

                size = pDataSize + recheckContentAdd

                if (additionalContent != recheckContentAdd) {
                    additionalContent = recheckContentAdd
                    recheckContentAdd = 0
                }
            }

            return size
        } else {
            return pDataSize
        }
    }

    fun getItemRealPosition(pPosition: Int): Int {
        return pPosition -
            if (pPosition > mFirstAdPosition && mAdDelta != 0) {
                var minus = 0

                if (pPosition > mFirstAdPosition) {
                    minus++

                    if (pPosition - mFirstAdPosition >= mAdDelta) {
                        minus += (pPosition - mFirstAdPosition) / (mAdDelta)
                    }
                }

                minus
            } else 0
    }
}

open class OneItemAdsAdapter<T>(
    @LayoutRes val pLayoutResource: Int,
    pData: List<T> = listOf(),
    private val mItemLoadListener: LoadItem<T>? = null,
    private val mAdapterSizeListener: RecyclerAdapterSizeListener? = null,
    private val mConfig: OIAdsAdapterConfig = OIAdsAdapterConfig(),
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val adItem = 999
    private val adMediaItem = 1999

    var data: List<T> = pData
        set(value) {
            val productDiffUtilCallback = OneItemDiffUtil(field, value)
            val productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback)

            field = value

            productDiffResult.dispatchUpdatesTo(this)
            mAdapterSizeListener?.setLoadedItems(value.size)
        }

    fun silenceSetData(items: List<T>) {
        data = items
        mAdapterSizeListener?.setLoadedItems(this.data.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            adItem -> AdItemViewHolder(parent.inflateById(R.layout.item_ad))
            adMediaItem -> MediaAdItemViewHolder(parent.inflateById(R.layout.item_media_ad))
            else -> {
                val nHolderItem =
                    LayoutInflater.from(parent.context).inflate(pLayoutResource, parent, false)
                CatOneTypeViewHolder(nHolderItem)
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
            holder.initNativeBanner(getAdForPosition(), mConfig.canShowAds())
        } else if (holder is MediaAdItemViewHolder) {
            holder.initNativeMediaBanner(getAdForPosition(), mConfig.canShowAds())
        } else {
            if (holder is CatOneTypeViewHolder)
                mItemLoadListener?.loadData(data[mConfig.getItemRealPosition(position)], holder)
        }
    }

    override fun getItemCount(): Int {
        mAdapterSizeListener?.setLoadedItems(data.size)
        return mConfig.calcRecyclerSize(data.size)
    }

    override fun getItemViewType(position: Int): Int {
        return if (mConfig.canPlaceAd(position)) {
            return if (mConfig.mUseMediaBanner) adMediaItem else adItem
        } else 0
    }

    fun interface LoadItem<T> {
        fun loadData(pData: T, pHolder: CatOneTypeViewHolder)
    }
}
