package dev.astler.unlib_test.activity.ads

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import dev.astler.cat_ui.activities.CatActivity
import dev.astler.cat_ui.adapters.CatOneTypeAdapter
import dev.astler.unlib_ads.utils.NativeAdsLoader
import dev.astler.unlib_ads.utils.getAdRequest
import dev.astler.unlib_ads.utils.initAds
import dev.astler.unlib_test.R
import dev.astler.unlib_test.databinding.ActivityRecyclerviewBinding
import dev.astler.unlib_test.databinding.ItemTextBinding
import dev.astler.unlib_test.items.ClickableItem

class AdsTestMenu : CatActivity<ActivityRecyclerviewBinding>(R.layout.activity_recyclerview) {
    private lateinit var mAdapter: CatOneTypeAdapter<ClickableItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(mViewBinding.root)

        initAds()

        NativeAdsLoader.instance?.loadAds(this, getAdRequest())

        mAdapter = CatOneTypeAdapter(
            R.layout.item_text,
            { pData, pHolder ->
                val nBind = ItemTextBinding.bind(pHolder.mItemView)
                nBind.text.text = pData.text

                nBind.root.setOnClickListener {
                    pData.pAction(it)
                }
            }
        )

        mAdapter.setData(
            listOf(
                ClickableItem("One Item List With Ad") {
                    startActivity(Intent(this, AdsOpenItemListActivity::class.java))
                },
            )
        )

        mViewBinding.list.layoutManager = LinearLayoutManager(this)
        mViewBinding.list.adapter = mAdapter
    }

    override fun getViewBinding() = ActivityRecyclerviewBinding.inflate(layoutInflater)
}
