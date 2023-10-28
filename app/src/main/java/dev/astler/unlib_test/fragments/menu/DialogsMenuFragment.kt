package dev.astler.unlib_test.fragments.menu

import dagger.hilt.android.AndroidEntryPoint
import dev.astler.cat_ui.utils.dialogs.confirmDialog
import dev.astler.cat_ui.utils.dialogs.exitDialog
import dev.astler.cat_ui.utils.dialogs.okDialog
import dev.astler.cat_ui.utils.dialogs.yesNoDialog
import dev.astler.catlib.extensions.toast
import dev.astler.unlib_compose.ui.compose.items.BaseCard
import dev.astler.unlib_test.R
import dev.astler.unlib_test.data.TestBaseItem

@AndroidEntryPoint
class DialogsMenuFragment : TestsMenuFragment() {
    private val yesNoEmptyDialogKey = "YesNoEmptyDialog"
    private val yesNoDialogKey = "YesNoDialog"
    private val exitDialogKey = "ExitDialog"
    private val confirmDialogKey = "ConfirmDialog"
    private val okDialogKey = "OkDialog"

    override val menuItems = listOf(
        BaseCard(TestBaseItem(R.string.yes_no_empty_dialog, R.drawable.ic_launcher_foreground, 2, uid = yesNoEmptyDialogKey)),
        BaseCard(TestBaseItem(R.string.yes_no_dialog, R.drawable.ic_launcher_foreground, 2, uid = yesNoDialogKey)),
        BaseCard(TestBaseItem(R.string.exit, R.drawable.ic_launcher_foreground, 2, uid = exitDialogKey)),
        BaseCard(TestBaseItem(R.string.confirm, R.drawable.ic_launcher_foreground, 2, uid = confirmDialogKey)),
        BaseCard(TestBaseItem(R.string.ok, R.drawable.ic_launcher_foreground, 2, uid = okDialogKey)),
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

            exitDialogKey -> {
                activity?.exitDialog(preferences)
            }

            confirmDialogKey -> {
                safeContext.confirmDialog(
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
        }
    }
}