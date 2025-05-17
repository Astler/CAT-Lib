package dev.astler.ui.fragments

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
import dev.astler.ui.interfaces.ICatActivity
import dev.astler.ui.interfaces.CoreFragmentInterface
import dev.astler.ui.utils.getStringResource
import dev.astler.catlib.preferences.PreferencesTool
import dev.astler.ui.theme.CatComposeTheme
import javax.inject.Inject

abstract class CatComposeFragment : Fragment(), CoreFragmentInterface, MenuProvider {

    @Inject
    lateinit var preferences: PreferencesTool

    protected var fragmentTag: String = this::class.java.simpleName
    protected var coreListener: ICatActivity? = null

    protected lateinit var safeContext: Context

    open val addMenuProvider: Boolean = true

    override fun onAttach(context: Context) {
        super.onAttach(context)

        safeContext = context

        if (context is ICatActivity)
            coreListener = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (addMenuProvider) {
            activity?.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }

        val composeView = ComposeView(safeContext).apply {
            setContent {
                CatComposeTheme() {
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

        if (addMenuProvider) {
            activity?.removeMenuProvider(this)
        }
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
