package dev.astler.unlib_test.fragments.menu

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.catlib.signin.utils.startMandatorySignIn
import dev.astler.catlib.signin.utils.startOptionalSignIn
import dev.astler.catlib.signin.utils.startRegisterSignIn
import com.ao.subscribeme.R
import dev.astler.ui.compose.items.BaseCard
import dev.astler.unlib_test.data.TestBaseItem

@AndroidEntryPoint
class ActivitiesMenuFragment : TestsMenuFragment() {

    private val startMandatorySignInKey = "StartMandatorySignIn"
    private val startOptionalSignInKey = "StartOptionalSignIn"
    private val startRegisterSignInKey = "StartRegisterSignIn"
    private val statusBarActionsFragmentKey = "StatusBarActionsFragment"

    override val menuItems = listOf(
        BaseCard(TestBaseItem(R.string.mandatory_sign_in, R.drawable.ic_launcher_foreground, 3, uid = startMandatorySignInKey)),
        BaseCard(TestBaseItem(R.string.optional_sign_in, R.drawable.ic_launcher_foreground, 3, uid = startOptionalSignInKey)),
        BaseCard(TestBaseItem(R.string.register, R.drawable.ic_launcher_foreground, 3, uid = startRegisterSignInKey)),
        BaseCard(TestBaseItem(R.string.status_bar_actions, R.drawable.ic_launcher_foreground, 3, uid = statusBarActionsFragmentKey)),
    )

    override fun menuItemClicked(uid: String) {
        when (uid) {
            startMandatorySignInKey -> {
                activity?.startMandatorySignIn()
            }

            startOptionalSignInKey -> {
                activity?.startOptionalSignIn()
            }

            startRegisterSignInKey -> {
                activity?.startRegisterSignIn()
            }

            statusBarActionsFragmentKey -> {
                findNavController().navigate(R.id.action_global_statusBarActionsFragment)
            }
        }
    }
}