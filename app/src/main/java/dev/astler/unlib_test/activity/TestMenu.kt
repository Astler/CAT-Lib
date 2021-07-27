package dev.astler.unlib_test.activity

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import dev.astler.unlib.adapters.BaseOneItemListAdapter
import dev.astler.unlib.utils.* // ktlint-disable no-wildcard-imports
import dev.astler.unlib_ads.activity.UnLibAdsActivity
import dev.astler.unlib_test.R
import dev.astler.unlib_test.activity.signin.SignInTestActivity
import dev.astler.unlib_test.databinding.ActivityRecyclerviewBinding
import dev.astler.unlib_test.databinding.ItemTextBinding
import dev.astler.unlib_test.items.ClickableItem

class TestMenu : UnLibAdsActivity() {

    private lateinit var mListBinding: ActivityRecyclerviewBinding
    private lateinit var mAdapter: BaseOneItemListAdapter<ClickableItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mListBinding = ActivityRecyclerviewBinding.inflate(layoutInflater)

        setContentView(mListBinding.root)

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
                ClickableItem("Coil Images Web") {
                    startActivity(Intent(this, ImageLoadersActivity::class.java))
                },
                ClickableItem("Sign In Activity") {
                    startActivity(Intent(this, SignInTestActivity::class.java))
                },
                ClickableItem("Shortcode Text") {
                    startActivity(Intent(this, ShortcodeTextActivity::class.java))
                },
                ClickableItem("Async List") {
                    startActivity(Intent(this, AsyncListActivity::class.java))
                },
                ClickableItem("Shake It") {
                    it.shake()
                },
                ClickableItem("Dialog") {
                    dialog("Title", "Message")
                },
                ClickableItem("Msg Dialog") {
                    dialog(pMsg = "Message")
                },
                ClickableItem("Ok Dialog") {
                    okDialog(
                        "Title", "Message",
                        {
                            makeToast("Action!")
                        }
                    )
                },
                ClickableItem("Confirm Dialog") {
                    confirmDialog(
                        "Title", "Message",
                        "Yes",
                        "No",
                        pPositiveAction = {
                            makeToast("Action Yes!")
                        },
                        pNegativeAction = {
                            makeToast("Action No!")
                        }
                    )
                },
                ClickableItem("Exit Dialog") {
                    exitDialog()
                },
                ClickableItem("Theme Utils") {
                    dialog("Theme Utils", "isSystemDarkTheme = ${isSystemDarkTheme()}\nisAppDarkTheme = ${isAppDarkTheme()}")
                },
                ClickableItem("Ads Utils") {
                    dialog("Ads Utils", "canShowAds = ${canShowAds()}")
                },
                ClickableItem("Services Is?") {
                    dialog("Services?", "google = ${getMobileServiceSource()}")
                },
                ClickableItem("StatusColor") {
                    startActivity(Intent(this, WindowTestActivity::class.java))
                },
                // TODO Search List Dialogs
            )
        )

        mListBinding.list.layoutManager = LinearLayoutManager(this)
        mListBinding.list.adapter = mAdapter
    }
}
