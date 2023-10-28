package dev.astler.unlib_test.fragments.menu

import androidx.navigation.fragment.findNavController
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.catlib.extensions.toast
import dev.astler.unlib_compose.ui.compose.items.BaseCard
import dev.astler.unlib_test.R
import dev.astler.unlib_test.data.TestBaseItem
import dev.astler.unlib_test.fragments.menu.TestsMenuFragment

@AndroidEntryPoint
class FirebaseMenuFragment : TestsMenuFragment() {

    private val messagingSubscribeKey = "MessagingSubscribe"
    private val testTopicToSubscribeKey = "cat_topic"
    private val analyticsKey = "Analytics"
    private val remoteConfigKey = "RemoteConfig"


    override val menuItems = listOf(
        BaseCard(TestBaseItem(R.string.messaging_subscribe, R.drawable.ic_launcher_foreground, 3, uid = messagingSubscribeKey)),
        BaseCard(TestBaseItem(R.string.analytics, R.drawable.ic_launcher_foreground, 3, uid = analyticsKey)),
        BaseCard(TestBaseItem(R.string.remote_config, R.drawable.ic_launcher_foreground, 4, uid = remoteConfigKey)),
    )

    override fun menuItemClicked(uid: String) {
        when (uid) {
            messagingSubscribeKey -> {
                Firebase.messaging.subscribeToTopic(testTopicToSubscribeKey)
                    .addOnCompleteListener { task ->
                        safeContext.toast(
                            safeContext.getString(
                                R.string.messaging_subscribe_result,
                                testTopicToSubscribeKey,
                                task.isSuccessful.toString()
                            )
                        )
                    }
            }

            analyticsKey -> {
                findNavController().navigate(R.id.action_global_analyticsFragment)
            }

            remoteConfigKey -> {
                findNavController().navigate(R.id.action_global_remoteConfigFragment)
            }
        }
    }
}