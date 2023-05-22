package dev.astler.unlib_test.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.cat_ui.activities.CatActivity
import dev.astler.unlib_compose.ui.mixed.TypewriterComposeFragment

@AndroidEntryPoint
class TypewriterActivity : CatActivity<ViewBinding>() {
    override fun inflateBinding(layoutInflater: LayoutInflater): ViewBinding {
        return ViewBinding { View(this) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, TypewriterComposeFragment())
            .commit()
    }
}