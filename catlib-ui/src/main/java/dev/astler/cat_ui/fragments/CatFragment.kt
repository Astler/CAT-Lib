package dev.astler.cat_ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.cat_ui.interfaces.ICatActivity
import dev.astler.cat_ui.interfaces.CoreFragmentInterface
import dev.astler.cat_ui.interfaces.IRootInsets
import dev.astler.cat_ui.utils.getStringResource
import dev.astler.catlib.preferences.PreferencesTool
import javax.inject.Inject

abstract class CatFragment<VB : ViewBinding> : Fragment(), CoreFragmentInterface, MenuProvider {

    @Inject
    lateinit var preferences: PreferencesTool

    protected var fragmentTag: String = this::class.java.name

    private var _binding: VB? = null

    protected var coreListener: ICatActivity? = null
    protected var rootInsets: IRootInsets? = null
    protected lateinit var safeContext: Context
    protected val binding get() = _binding!!

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
    open val addMenuProvider: Boolean = true

    override fun onAttach(context: Context) {
        super.onAttach(context)

        safeContext = context

        if (context is ICatActivity)
            coreListener = context

        if (context is IRootInsets)
            rootInsets = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)

        if (addMenuProvider) {
            activity?.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        coreListener?.setCurrentFragment(this)
    }

    override fun onDestroy() {
        super.onDestroy()

//        if (addMenuProvider) {
//            activity?.removeMenuProvider(this)
//        }
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
