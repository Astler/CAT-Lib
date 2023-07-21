package dev.astler.unlib_test.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.cat_ui.activities.CatActivity
import dev.astler.catlib.utils.canShowAds
import dev.astler.ads.adapters.OIAdsAdapterConfig
import dev.astler.ads.adapters.OneItemAdsAdapter
import dev.astler.ads.initialization.AdsTool
import dev.astler.ads.utils.NativeAdsLoader
import dev.astler.unlib_test.R
import dev.astler.unlib_test.databinding.ActivityRecyclerviewBinding
import dev.astler.unlib_test.items.TextItem
import javax.inject.Inject

@AndroidEntryPoint
class AdsOpenItemListActivity : CatActivity<ActivityRecyclerviewBinding>() {

    @Inject lateinit var adsTool: AdsTool

    private lateinit var mAdapter: OneItemAdsAdapter<TextItem>

    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityRecyclerviewBinding {
        return ActivityRecyclerviewBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adsTool.startNativeAdsLoader()

        mAdapter = OneItemAdsAdapter(
            R.layout.item_text,
            mItemLoadListener = { pData, pHolder ->
                val nBind = dev.astler.unlib_test.databinding.ItemTextBinding.bind(pHolder.mItemView)
                nBind.text.text = pData.text
            },
            mConfig = OIAdsAdapterConfig(canShowAds(preferencesTool))
        )

        mAdapter.data =
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

        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.adapter = mAdapter
    }
}