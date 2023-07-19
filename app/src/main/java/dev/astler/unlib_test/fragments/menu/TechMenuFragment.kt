package dev.astler.unlib_test.fragments.menu

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.cat_ui.utils.dialogs.okDialog
import dev.astler.cat_ui.utils.isAppDarkTheme
import dev.astler.cat_ui.utils.isSystemDarkMode
import dev.astler.catlib.gPreferencesTool
import dev.astler.catlib.utils.canShowAds
import dev.astler.catlib.utils.getMobileServiceSource
import dev.astler.catlib.utils.infoLog
import dev.astler.catlib.utils.isDebuggable
import dev.astler.catlib.utils.isOnline
import dev.astler.catlib.utils.isPackageInstalled
import dev.astler.catlib.utils.isPackageInstalledAlt
import dev.astler.unlib_compose.ui.compose.items.BaseCard
import dev.astler.unlib_test.R
import dev.astler.unlib_test.data.TestBaseItem
import dev.astler.unlib_test.items.ClickableItem

@AndroidEntryPoint
class TechMenuFragment : TestsMenuFragment() {

    private val servicesInfoKey = "ServicesInfo"
    private val languagesPreviewKey = "LanguagesPreview"
    private val debuggableInfoKey = "DebuggableInfo"
    private val myAppsTrackerInfoKey = "MyAppsTrackerInfo"
    private val themePreviewKey = "ThemePreview"

    override val menuItems = listOf(
        BaseCard(TestBaseItem(dev.astler.catlib.ui.R.string.app_theme, R.drawable.ic_launcher_foreground, 3, uid = themePreviewKey)),
        BaseCard(TestBaseItem(dev.astler.catlib.ui.R.string.language, R.drawable.ic_launcher_foreground, 3, uid = languagesPreviewKey)),
        BaseCard(TestBaseItem(R.string.debuggable_info, R.drawable.ic_launcher_foreground, 3, uid = debuggableInfoKey)),
        BaseCard(TestBaseItem(R.string.my_apps_tracker, R.drawable.ic_launcher_foreground, 3, uid = myAppsTrackerInfoKey)),
        BaseCard(TestBaseItem(R.string.services_info, R.drawable.ic_launcher_foreground, 3, uid = servicesInfoKey)),
    )

    override fun menuItemClicked(uid: String) {
        when (uid) {
            servicesInfoKey -> {
                safeContext.okDialog(
                    title = "Services?",
                    message = "google = ${safeContext.getMobileServiceSource()}\n\nIs online: ${safeContext.isOnline}"
                )
            }

            languagesPreviewKey -> {
                findNavController().navigate(R.id.action_global_languagePreviewFragment)
            }

            themePreviewKey -> {
                findNavController().navigate(R.id.action_global_themePreviewFragment)
            }

            debuggableInfoKey -> {
                safeContext.okDialog("is debuggable: ${safeContext.isDebuggable()}")
            }

            myAppsTrackerInfoKey -> {
                safeContext.okDialog(
                    """
                    is installed Inventory Editor ${safeContext.isPackageInstalled("dev.astler.inveditormc")}
                    is installed Alt Inventory Editor ${safeContext.isPackageInstalledAlt("dev.astler.inveditormc")}
                """.trimIndent()
                )
            }
        }
    }
}