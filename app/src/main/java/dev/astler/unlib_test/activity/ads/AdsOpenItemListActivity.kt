package dev.astler.unlib_test.activity.ads

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import dev.astler.unlib_ads.activity.UnLibAdsActivity
import dev.astler.unlib_ads.adapters.OIAdsAdapterConfig
import dev.astler.unlib_ads.adapters.OneItemAdsAdapter
import dev.astler.unlib_ads.utils.NativeAdsLoader
import dev.astler.unlib_ads.utils.canShowAds
import dev.astler.unlib_test.R
import dev.astler.unlib_test.databinding.ActivityRecyclerviewBinding
import dev.astler.unlib_test.items.TextItem

class AdsOpenItemListActivity : UnLibAdsActivity() {

    private lateinit var mImagesBinding: ActivityRecyclerviewBinding
    private lateinit var mAdapter: OneItemAdsAdapter<TextItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mImagesBinding = ActivityRecyclerviewBinding.inflate(layoutInflater)

        setContentView(mImagesBinding.root)

        NativeAdsLoader.instance?.loadAds(this, getAdRequest())

        mAdapter = OneItemAdsAdapter(
            R.layout.item_text,
            mItemLoadListener = { pData, pHolder ->
                val nBind = dev.astler.unlib_test.databinding.ItemTextBinding.bind(pHolder.mItemView)
                nBind.text.text = pData.text
            },
            mConfig = OIAdsAdapterConfig(canShowAds())
        )

        mAdapter.setData(
            listOf(
                TextItem("A 1"),
                TextItem("A 2"),
                TextItem("A 3"),
                TextItem("A 4"),
                TextItem("A 5"),
                TextItem("A 6"),
                TextItem("A 7"),
                TextItem("A 8"),
                TextItem("A 9"),
                TextItem("A 10"),
                TextItem("A 11"),
                TextItem("A 12"),
                TextItem("A 13"),
                TextItem("A 14"),
                TextItem("A 15"),
                TextItem("A 16"),
                TextItem("A 17"),
                TextItem("A 18"),
                TextItem("A 19"),
                TextItem("A 20")
            )
        )

        mImagesBinding.list.layoutManager = LinearLayoutManager(this)
        mImagesBinding.list.adapter = mAdapter
    }
}
