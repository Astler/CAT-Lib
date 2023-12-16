package dev.astler.unlib_test

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.billing.data.BillingViewModel
import dev.astler.cat_ui.activities.BindingCatActivity
import dev.astler.catlib.constants.IODispatcher
import dev.astler.catlib.extensions.getJsonContent
import dev.astler.catlib.helpers.infoLog
import dev.astler.unlib_test.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BindingCatActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val _billingViewModel: BillingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch(IODispatcher) {
            val dataBe = "https://astler.net/apps_data/be_versions.json".getJsonContent()
            infoLog(dataBe)
        }
    }
}
