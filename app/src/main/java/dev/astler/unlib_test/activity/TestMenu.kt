package dev.astler.unlib_test.activity

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.zeugmasolutions.localehelper.Locales
import dev.astler.cat_ui.activities.CatActivity
import dev.astler.cat_ui.adapters.CatOneTypeAdapter
import dev.astler.cat_ui.utils.* // ktlint-disable no-wildcard-imports
import dev.astler.unlib.gPreferencesTool
import dev.astler.unlib.signin.utils.startMandatorySignIn
import dev.astler.unlib.signin.utils.startOptionalSignIn
import dev.astler.unlib.signin.utils.startRegisterSignIn
import dev.astler.unlib.utils.* // ktlint-disable no-wildcard-imports
import dev.astler.unlib_test.R
import dev.astler.unlib_test.activity.signin.SignInTestActivity
import dev.astler.unlib_test.databinding.ActivityRecyclerviewBinding
import dev.astler.unlib_test.databinding.ItemTextBinding
import dev.astler.unlib_test.items.ClickableItem
import java.util.*

class TestMenu : CatActivity() {

    private lateinit var mAdapter: CatOneTypeAdapter<ClickableItem>

    private lateinit var mViewBinding: ActivityRecyclerviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewBinding = ActivityRecyclerviewBinding.inflate(layoutInflater)

        setContentView(mViewBinding.root)

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
                ClickableItem("Theme Utils") {
                    dialog(
                        "Theme Utils",
                        "isSystemDarkTheme = ${isSystemDarkMode}\nisAppDarkTheme = ${isAppDarkTheme()}\npreferencesSetting = ${gPreferencesTool.appTheme}"
                    )
                },
                ClickableItem("RU") {
                    updateLocale(Locales.Russian)
                },
                ClickableItem("EN") {
                    updateLocale(Locale.ENGLISH)
                },
                ClickableItem("Theme Utils") {
                    dialog(
                        "Theme Utils",
                        "isSystemDarkTheme = ${isSystemDarkMode}\nisAppDarkTheme = ${isAppDarkTheme()}\npreferencesSetting = ${gPreferencesTool.appTheme}"
                    )
                },
                ClickableItem("Set Dark Theme") {
                    gPreferencesTool.appTheme = "dark"
                },
                ClickableItem("Set Light Theme") {
                    gPreferencesTool.appTheme = "light"
                },
                ClickableItem("Set System Theme") {
                    gPreferencesTool.appTheme = "system"
                },
                ClickableItem("Set AUTO Theme") {
                    gPreferencesTool.appTheme = "auto"
                },
                ClickableItem("Coil Images Web") {
                    startActivity(Intent(this, ImageLoadersActivity::class.java))
                },
                ClickableItem("Sign In TEST Activity") {
                    startActivity(Intent(this, SignInTestActivity::class.java))
                },
                ClickableItem("Mandatory Sign In Activity") {
                    startMandatorySignIn()
                },
                ClickableItem("Optional Sign In Activity") {
                    startOptionalSignIn()
                },
                ClickableItem("Register Activity") {
                    startRegisterSignIn()
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
                            toast("Action!")
                        }
                    )
                },
                ClickableItem("Confirm Dialog") {
                    confirmDialog(
                        "Title", "Message",
                        "Yes",
                        "No",
                        pPositiveAction = {
                            toast("Action Yes!")
                        },
                        pNegativeAction = {
                            toast("Action No!")
                        }
                    )
                },
                ClickableItem("Exit Dialog") {
                    exitDialog()
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

        mViewBinding.list.layoutManager = LinearLayoutManager(this)
        mViewBinding.list.adapter = mAdapter
    }
}
