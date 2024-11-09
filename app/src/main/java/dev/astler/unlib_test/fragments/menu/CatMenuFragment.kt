package dev.astler.unlib_test.fragments.menu

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.ui.utils.dialogs.okDialog
import dev.astler.catlib.config.AppConfig
import com.ao.subscribeme.R
import dev.astler.catlib.signin.SignInManager
import dev.astler.ui.compose.items.BaseCard
import dev.astler.unlib_test.data.TestBaseItem
import javax.inject.Inject

@AndroidEntryPoint
class CatMenuFragment : TestsMenuFragment() {

    @Inject
    lateinit var appConfig: AppConfig

    @Inject
    lateinit var signInManager: SignInManager

    private val appConfigPreviewKey = "AppConfigPreview"
    private val catCodeKey = "CatCode"
    private val signInV1Key = "SignInV1"
    private val signInV2Key = "SignInV2"
    private val signInV3Key = "SignInV3"

    override val menuItems = listOf(
        BaseCard(
            TestBaseItem(
                R.string.app_config_preview,
                R.drawable.ic_launcher_foreground,
                2,
                uid = appConfigPreviewKey
            )
        ),
        BaseCard(
            TestBaseItem(
                R.string.code,
                R.drawable.ic_launcher_foreground,
                2,
                uid = catCodeKey
            )
        ),
        BaseCard(
            TestBaseItem(
                R.string.sign_in_v1,
                R.drawable.ic_launcher_foreground,
                2,
                uid = signInV1Key
            )
        ),
        BaseCard(
            TestBaseItem(
                R.string.sign_in_v2,
                R.drawable.ic_launcher_foreground,
                2,
                uid = signInV2Key
            )
        ),
        BaseCard(
            TestBaseItem(
                R.string.sign_in_v3,
                R.drawable.ic_launcher_foreground,
                2,
                uid = signInV3Key
            )
        ),
    )

    override fun menuItemClicked(uid: String) {
        when (uid) {
            appConfigPreviewKey -> {
                safeContext.okDialog(
                    message =
                    """App config for this app:
                    
$appConfig
                """.trimIndent()
                )
            }

            catCodeKey -> {
                findNavController().navigate(R.id.action_global_catCodeFragment)
            }

            signInV1Key -> {
                signInManager.tryCredentialSignIn()
            }

            signInV2Key -> {
                signInManager.tryGoogleDialogSignIn()
            }

            signInV3Key -> {
                signInManager.tryEmailSignIn()
            }
        }
    }
}