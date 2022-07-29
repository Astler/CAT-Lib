package dev.astler.cat_ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import dev.astler.cat_ui.interfaces.ActivityInterface
import dev.astler.cat_ui.interfaces.CoreFragmentInterface
import dev.astler.cat_ui.utils.getStringResource

abstract class CatFragmentLegacy(pLayoutId: Int = 0) : Fragment(pLayoutId), CoreFragmentInterface,
    MenuProvider {

    var coreListener: ActivityInterface? = null
    protected lateinit var safeContext: Context
    open val addMenuProvider: Boolean = true

    override fun onAttach(pContext: Context) {
        super.onAttach(pContext)

        safeContext = pContext

        if (pContext is ActivityInterface)
            coreListener = pContext
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (addMenuProvider) {
            activity?.addMenuProvider(this)
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun backPressed() {
        coreListener?.onFragmentBackPressed()
    }

    override fun onResume() {
        super.onResume()
        coreListener?.setCurrentFragment(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (addMenuProvider) {
            activity?.removeMenuProvider(this)
        }
    }

    fun getStringByName(pName: String, pReturnDef: String = ""): String {
        return safeContext.getStringResource(pName, returnDef = pReturnDef)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
    }

    override fun onMenuItemSelected(menuItem: MenuItem) = false
}
