package dev.astler.cat_ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import dev.astler.cat_ui.interfaces.ActivityInterface
import dev.astler.cat_ui.interfaces.CoreFragmentInterface
import dev.astler.cat_ui.utils.getStringResource

abstract class CatFragment<VB : ViewBinding>(pLayoutId: Int = 0) : Fragment(pLayoutId),
    CoreFragmentInterface,
    MenuProvider {

    private var _binding: VB? = null

    protected var coreListener: ActivityInterface? = null
    protected lateinit var safeContext: Context
    protected var binding = _binding!!

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
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
        _binding = bindingInflater.invoke(inflater, container, false)

        if (addMenuProvider) {
            activity?.addMenuProvider(this)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        coreListener?.setCurrentFragment(this)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {}

    override fun onMenuItemSelected(menuItem: MenuItem) = false

    fun backPressed() {
        coreListener?.onFragmentBackPressed()
    }

    fun getStringByName(pName: String, pReturnDef: String = ""): String {
        return safeContext.getStringResource(pName, returnDef = pReturnDef)
    }
}
