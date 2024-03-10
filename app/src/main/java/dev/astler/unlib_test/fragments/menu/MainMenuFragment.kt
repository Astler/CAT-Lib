package dev.astler.unlib_test.fragments.menu

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.catlib.signin.SignInTool
import dev.astler.unlib_compose.ui.compose.items.BaseCard
import dev.astler.unlib_compose.ui.compose.items.ElevatedBaseCard
import dev.astler.unlib_compose.ui.compose.items.FilledBaseCard
import com.ao.subscribeme.R
import dev.astler.unlib_test.data.TestBaseItem
import javax.inject.Inject

@AndroidEntryPoint
class MainMenuFragment : TestsMenuFragment() {

    private val composeKey = "Compose"
    private val baseDialogsKey = "BaseDialogs"
    private val viewsKey = "Views"
    private val techKey = "Tech"
    private val fragmentsKey = "Fragments"
    private val activitiesKey = "Activities"
    private val adsKey = "Ads"
    private val firebaseKey = "Firebase"
    private val catKey = "Cat"

    override val menuItems = listOf(
        BaseCard(TestBaseItem(R.string.activities, R.drawable.ic_launcher_foreground, 3, uid = activitiesKey)),
        FilledBaseCard(TestBaseItem(R.string.fragments, R.drawable.ic_launcher_foreground, 3, uid = fragmentsKey)),
        ElevatedBaseCard(TestBaseItem(R.string.compose, R.drawable.ic_launcher_foreground, 3, uid = composeKey)),
        FilledBaseCard(TestBaseItem(R.string.dialogs, R.drawable.ic_launcher_foreground, 3, uid = baseDialogsKey)),
        BaseCard(TestBaseItem(R.string.views, R.drawable.ic_launcher_foreground, 2, uid = viewsKey)),
        FilledBaseCard(TestBaseItem(R.string.ads, R.drawable.ic_launcher_foreground, 2, uid = adsKey)),
        ElevatedBaseCard(TestBaseItem(R.string.tech, R.drawable.ic_launcher_foreground, 2, uid = techKey)),
        FilledBaseCard(TestBaseItem(R.string.firebase, R.drawable.ic_launcher_foreground, 3, uid = firebaseKey)),
        BaseCard(TestBaseItem(R.string.cat, R.drawable.ic_launcher_foreground, 3, uid = catKey)),
    )

    override fun menuItemClicked(uid: String) {
        when (uid) {
            composeKey -> {
                findNavController().navigate(R.id.action_global_composeMenuFragment)
            }

            baseDialogsKey -> {
                findNavController().navigate(R.id.action_global_dialogsMenuFragment)
            }

            viewsKey -> {
                findNavController().navigate(R.id.action_global_viewsMenuFragment)
            }

            techKey -> {
                findNavController().navigate(R.id.action_global_techMenuFragment)
            }

            fragmentsKey -> {
                findNavController().navigate(R.id.action_global_fragmentsMenuFragment)
            }

            activitiesKey -> {
                findNavController().navigate(R.id.action_global_activitiesMenuFragment)
            }

            adsKey -> {
                findNavController().navigate(R.id.action_global_adsMenuFragment)
            }

            firebaseKey -> {
                findNavController().navigate(R.id.action_global_firebaseMenuFragment)
            }

            catKey -> {
                findNavController().navigate(R.id.action_global_catMenuFragment)
            }
        }
    }
}