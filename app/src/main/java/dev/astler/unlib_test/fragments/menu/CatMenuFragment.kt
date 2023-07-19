package dev.astler.unlib_test.fragments.menu

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.cat_ui.utils.dialogs.okDialog
import dev.astler.catlib.config.AppConfig
import dev.astler.unlib_compose.ui.compose.items.BaseCard
import dev.astler.unlib_test.R
import dev.astler.unlib_test.data.TestBaseItem
import javax.inject.Inject

@AndroidEntryPoint
class CatMenuFragment: TestsMenuFragment() {

    @Inject
    lateinit var appConfig: AppConfig

    private val appConfigPreviewKey = "AppConfigPreview"
    private val catCodeKey = "CatCode"

    override val menuItems = listOf(
        BaseCard(TestBaseItem(R.string.app_config_preview, R.drawable.ic_launcher_foreground, 2, uid = appConfigPreviewKey)),
        BaseCard(TestBaseItem(R.string.code, R.drawable.ic_launcher_foreground, 2, uid = catCodeKey)),
    )

    override fun menuItemClicked(uid: String) {
        when (uid) {
            appConfigPreviewKey -> {
                safeContext.okDialog(message =
                """App config for this app:
                    
$appConfig
                """.trimIndent())
            }

            catCodeKey -> {
                findNavController().navigate(R.id.action_global_catCodeFragment)
            }
        }
    }
}