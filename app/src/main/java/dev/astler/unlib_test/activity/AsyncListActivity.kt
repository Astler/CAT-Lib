package dev.astler.unlib_test.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import dev.astler.unlib.adapters.OILAsyncAdapter
import dev.astler.unlib_ads.activity.UnLibAdsActivity
import dev.astler.unlib_test.R
import dev.astler.unlib_test.databinding.ActivityRecyclerviewBinding
import dev.astler.unlib_test.databinding.ItemImageBinding
import dev.astler.unlib_test.items.ImageItem

class AsyncListActivity : UnLibAdsActivity() {

    private lateinit var mListBinding: ActivityRecyclerviewBinding
    private lateinit var mAdapter: OILAsyncAdapter<ImageItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mListBinding = ActivityRecyclerviewBinding.inflate(layoutInflater)

        setContentView(mListBinding.root)

        mAdapter = OILAsyncAdapter(
            R.layout.item_image,
            { pData, pHolder ->
                val nBind = ItemImageBinding.bind(pHolder.itemView)
                nBind.image.load(pData.link)
            }
        )

        mAdapter.addItems(
            listOf(
                ImageItem("https://cdn.pixabay.com/photo/2018/05/26/21/21/blue-berries-3432295_960_720.jpg"),
                ImageItem("https://cdn.pixabay.com/photo/2021/05/22/20/33/woman-6274584_960_720.jpg"),
                ImageItem("https://cdn.pixabay.com/photo/2021/04/22/04/09/rapeseed-6197976_960_720.jpg"),
                ImageItem("https://cdn.pixabay.com/photo/2020/09/20/16/27/model-5587623_960_720.jpg"),
                ImageItem("https://cdn.pixabay.com/photo/2020/02/13/06/49/seascape-4844697_960_720.jpg"),
                ImageItem("https://cdn.pixabay.com/photo/2021/05/17/05/21/flower-6259783_960_720.jpg"),
            )
        )

        mListBinding.list.layoutManager = LinearLayoutManager(this)
        mListBinding.list.adapter = mAdapter
    }
}
