package dev.astler.unlib_test.fragments.menu

import dagger.hilt.android.AndroidEntryPoint
import dev.astler.ui.utils.dialogs.okDialog
import dev.astler.ui.utils.dialogs.unpackedCatDialog
import dev.astler.ui.utils.dialogs.yesNoDialog
import dev.astler.catlib.extensions.toast
import com.ao.subscribeme.R
import dev.astler.ui.utils.dialogs.privacyPolicyDialog
import dev.astler.catlib.config.AppConfig
import dev.astler.ui.compose.items.BaseCard
import dev.astler.unlib_test.data.TestBaseItem
import javax.inject.Inject

@AndroidEntryPoint
class DialogsMenuFragment : TestsMenuFragment() {
    @Inject
    lateinit var appConfig: AppConfig

    private val yesNoEmptyDialogKey = "YesNoEmptyDialog"
    private val yesNoDialogKey = "YesNoDialog"
    private val confirmDialogKey = "ConfirmDialog"
    private val okDialogKey = "OkDialog"
    private val policyKey = "policy"

    override val menuItems = listOf(
        BaseCard(TestBaseItem(R.string.yes_no_empty_dialog, R.drawable.ic_launcher_foreground, 3, uid = yesNoEmptyDialogKey)),
        BaseCard(TestBaseItem(R.string.yes_no_dialog, R.drawable.ic_launcher_foreground, 3, uid = yesNoDialogKey)),
        BaseCard(TestBaseItem(R.string.confirm, R.drawable.ic_launcher_foreground, 3, uid = confirmDialogKey)),
        BaseCard(TestBaseItem(R.string.ok, R.drawable.ic_launcher_foreground, 3, uid = okDialogKey)),
        BaseCard(TestBaseItem(gg.pressf.resources.R.string.privacy_policy, R.drawable.ic_launcher_foreground, 6, uid = policyKey)),
    )

    override fun menuItemClicked(uid: String) {
        when (uid) {
            yesNoDialogKey -> {
                safeContext.yesNoDialog(title = "Test Title", message = "Update") {
                    safeContext.toast("Hello!")
                }
            }

            yesNoEmptyDialogKey -> {
                safeContext.yesNoDialog {
                    safeContext.toast("Hello!")
                }
            }

            confirmDialogKey -> {
                safeContext.unpackedCatDialog(
                    title = "Title",
                    message = "Message",
                    positive = "Yes",
                    negative = "No",
                    positiveAction = {
                        safeContext.toast("Action Yes!")
                    },
                    negativeAction = {
                        safeContext.toast("Action No!")
                    }
                )
            }

            okDialogKey -> {
                safeContext.okDialog(title = R.string.app_name)
            }

            policyKey -> {
                activity?.privacyPolicyDialog(appConfig, preferences)
            }
        }
    }
}