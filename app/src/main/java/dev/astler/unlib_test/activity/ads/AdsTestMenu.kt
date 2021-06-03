package dev.astler.unlib_test.activity.ads

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import dev.astler.unlib.adapters.BaseOneItemListAdapter
import dev.astler.unlib_ads.activity.UnLibAdsActivity
import dev.astler.unlib_ads.utils.NativeAdsLoader
import dev.astler.unlib_test.R
import dev.astler.unlib_test.databinding.ActivityRecyclerviewBinding
import dev.astler.unlib_test.databinding.ItemTextBinding
import dev.astler.unlib_test.items.ClickableItem

class AdsTestMenu : UnLibAdsActivity() {

    private lateinit var mListBinding: ActivityRecyclerviewBinding
    private lateinit var mAdapter: BaseOneItemListAdapter<ClickableItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mListBinding = ActivityRecyclerviewBinding.inflate(layoutInflater)

        setContentView(mListBinding.root)

        NativeAdsLoader.instance?.loadAds(this, getAdRequest())

        mAdapter = BaseOneItemListAdapter(
            R.layout.item_text,
            { pData, pHolder ->
                val nBind = ItemTextBinding.bind(pHolder.mItemView)
                nBind.text.text = pData.text

                nBind.root.setOnClickListener {
                    pData.pAction(it)
                }
            }
        )

        mAdapter.addItems(
            listOf(
                ClickableItem("One Item List With Ad") {
                    startActivity(Intent(this, AdsOpenItemListActivity::class.java))
                },
            )
        )

        mListBinding.list.layoutManager = LinearLayoutManager(this)
        mListBinding.list.adapter = mAdapter
    }
}
