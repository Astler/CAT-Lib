package dev.astler.cat_ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class CatBindingActivity<VB : ViewBinding>(pLayoutId: Int = 0) : AppCompatActivity(pLayoutId) {

    protected lateinit var mViewBinding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewBinding = getViewBinding()
    }

    abstract fun getViewBinding(): VB
}
