package dev.astler.unlib_test

import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.billing.data.BillingViewModel
import dev.astler.cat_ui.activities.BindingCatActivity
import dev.astler.unlib_test.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : BindingCatActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val _billingViewModel: BillingViewModel by viewModels()
}
