package dev.astler.unlib_test

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.ao.subscribeme.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.billing.data.BillingViewModel
import dev.astler.cat_ui.activities.BindingCatActivity
import dev.astler.cat_ui.extensions.InAppReviewActivityExtension
import dev.astler.cat_ui.extensions.InAppUpdateActivityExtension
import dev.astler.cat_ui.extensions.NetworkActivityExtension
import dev.astler.cat_ui.interfaces.INetworkActivity
import dev.astler.catlib.constants.IODispatcher
import dev.astler.catlib.extensions.getJsonContent
import dev.astler.catlib.extensions.toast
import dev.astler.catlib.helpers.infoLog
import dev.astler.catlib.signin.SignInTool
import dev.astler.catlib.signin.interfaces.ISignInListener
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BindingCatActivity<ActivityMainBinding>(ActivityMainBinding::inflate), ISignInListener, INetworkActivity {

    @Inject
    lateinit var singInTool: SignInTool

    private val _billingViewModel: BillingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        singInTool.tryToSignIn()

        InAppReviewActivityExtension(this, binding.snackbarLayout)
        InAppUpdateActivityExtension(this, binding.snackbarLayout)
        NetworkActivityExtension(this, binding.snackbarLayout)

        lifecycleScope.launch(IODispatcher) {
            val dataBe = "https://astler.net/apps_data/be_versions.json".getJsonContent()
            infoLog(dataBe)
        }
    }

    override fun updateUI(user: FirebaseUser?) {
        if (user == null) return

        toast("User: $user")
    }
}
