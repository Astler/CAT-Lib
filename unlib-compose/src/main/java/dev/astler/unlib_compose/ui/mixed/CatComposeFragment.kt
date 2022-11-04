package dev.astler.unlib_compose.ui.mixed

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import dev.astler.cat_ui.interfaces.ICatActivity
import dev.astler.cat_ui.interfaces.CoreFragmentInterface
import dev.astler.cat_ui.utils.getStringResource
import dev.astler.cat_ui.utils.isAppDarkTheme
import dev.astler.unlib_compose.theme.UnlibComposeTheme

abstract class CatComposeFragment : Fragment(), CoreFragmentInterface, MenuProvider {

    protected var coreListener: ICatActivity? = null
    protected lateinit var safeContext: Context

    open var fragmentTag = this::class.java.name
    open val addMenuProvider: Boolean = true

    override fun onAttach(pContext: Context) {
        super.onAttach(pContext)

        safeContext = pContext

        if (pContext is ICatActivity)
            coreListener = pContext
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (addMenuProvider) {
            activity?.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }

        val composeView = ComposeView(requireContext()).apply {
            setContent {
                UnlibComposeTheme(requireContext().isAppDarkTheme()) {
                    ScreenContent()
                }
            }
        }

        return composeView
    }

    @Composable
    open fun ScreenContent() {}

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
