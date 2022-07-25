package dev.astler.cat_ui.fragments

import android.content.Context
import androidx.fragment.app.Fragment
import dev.astler.cat_ui.interfaces.ActivityInterface
import dev.astler.cat_ui.interfaces.CoreFragmentInterface
import dev.astler.cat_ui.utils.getStringResource

abstract class CatFragmentLegacy(pLayoutId: Int = 0) : Fragment(pLayoutId), CoreFragmentInterface {

    var coreListener: ActivityInterface? = null
    protected lateinit var safeContext: Context

    override fun onAttach(pContext: Context) {
        super.onAttach(pContext)

        safeContext = pContext

        if (pContext is ActivityInterface)
            coreListener = pContext
    }

    fun backPressed() {
        coreListener?.onFragmentBackPressed()
    }

    override fun onResume() {
        super.onResume()
        coreListener?.setCurrentFragment(this)
    }

    fun getStringByName(pName: String, pReturnDef: String = ""): String {
        return safeContext.getStringResource(pName, returnDef = pReturnDef)
    }
}
