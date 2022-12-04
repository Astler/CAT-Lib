package dev.astler.unlib_test.activity

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.zeugmasolutions.localehelper.Locales
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.cat_ui.activities.CatActivity
import dev.astler.cat_ui.adapters.CatOneTypeAdapter
import dev.astler.cat_ui.utils.* 
import dev.astler.cat_ui.utils.dialogs.confirmDialog
import dev.astler.cat_ui.utils.dialogs.exitDialog
import dev.astler.cat_ui.utils.dialogs.okDialog
import dev.astler.cat_ui.utils.dialogs.yesNoDialog
import dev.astler.catlib.gPreferencesTool
import dev.astler.catlib.signin.utils.startMandatorySignIn
import dev.astler.catlib.signin.utils.startOptionalSignIn
import dev.astler.catlib.signin.utils.startRegisterSignIn
import dev.astler.catlib.utils.* 
import dev.astler.unlib_test.R
import dev.astler.unlib_test.activity.signin.SignInTestActivity
import dev.astler.unlib_test.databinding.ActivityRecyclerviewBinding
import dev.astler.unlib_test.databinding.ItemTextBinding
import dev.astler.unlib_test.items.ClickableItem
import java.util.*

@AndroidEntryPoint
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
                ClickableItem("Yes no empty dialog") {
                    yesNoDialog() {
                        toast("Hello!")
                    }
                },
                ClickableItem("Yes no string/id dialog") {
                    yesNoDialog(title = "Test Title", message = R.string.UPDATE) {
                        toast("Hello!")
                    }
                },
                ClickableItem("Confirm dialog") {
                    confirmDialog(
                        title = R.string.app_name,
                        message = R.string.rate_app,
                        positive = R.string.about,
                        negative = R.string.already_leave,
                        negativeAction = {
                            toast("Bye!")
                        },
                        positiveAction = {
                            toast("Hello!")
                        })
                },
                ClickableItem("Exit dialog") {
                    exitDialog()
                },
                ClickableItem("Theme Utils") {
                    okDialog(
                        title = "Theme Utils",
                        message = "isSystemDarkTheme = ${isSystemDarkMode}\nisAppDarkTheme = ${isAppDarkTheme()}\npreferencesSetting = ${gPreferencesTool.appTheme}"
                    )
                },
                ClickableItem("Confirm Dialog") {
                    confirmDialog(
                        title = "Title", 
                        message = "Message",
                        positive = "Yes",
                        negative = "No",
                        positiveAction = {
                            toast("Action Yes!")
                        },
                        negativeAction = {
                            toast("Action No!")
                        }
                    )
                },
                ClickableItem("Ads Utils") {
                    okDialog(title = "Ads Utils", message = "canShowAds = ${canShowAds()}")
                },
                ClickableItem("Services Is?") {
                    okDialog(title = "Services?", message = "google = ${getMobileServiceSource()}")
                },
                ClickableItem("---------------") { },
                ClickableItem("RU") {
                    updateLocale(Locales.Russian)
                },
                ClickableItem("EN") {
                    updateLocale(Locale.ENGLISH)
                },
                ClickableItem("Theme Utils") {
                    okDialog(
                        title = "Theme Utils",
                        message = "isSystemDarkTheme = ${isSystemDarkMode}\nisAppDarkTheme = ${isAppDarkTheme()}\npreferencesSetting = ${gPreferencesTool.appTheme}"
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

                ClickableItem("StatusColor") {
                    startActivity(Intent(this, WindowTestActivity::class.java))
                }
            )
        )

        mViewBinding.list.layoutManager = LinearLayoutManager(this)
        mViewBinding.list.adapter = mAdapter
    }
}
