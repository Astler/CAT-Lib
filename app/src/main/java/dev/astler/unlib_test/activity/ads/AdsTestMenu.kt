package dev.astler.unlib_test.activity.ads

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.ads.initialization.AdsTool
import dev.astler.cat_ui.activities.CatActivity
import dev.astler.cat_ui.adapters.CatOneTypeAdapter
import dev.astler.unlib_test.R
import dev.astler.unlib_test.databinding.ActivityRecyclerviewBinding
import dev.astler.unlib_test.databinding.ItemTextBinding
import dev.astler.unlib_test.items.ClickableItem
import javax.inject.Inject

@AndroidEntryPoint
class AdsTestMenu : CatActivity() {
    @Inject
    lateinit var adsTool: AdsTool

    private lateinit var mAdapter: CatOneTypeAdapter<ClickableItem>

    private lateinit var mViewBinding: ActivityRecyclerviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewBinding = ActivityRecyclerviewBinding.inflate(layoutInflater)

        setContentView(mViewBinding.root)

        adsTool.startNativeAdsLoader()

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
}
