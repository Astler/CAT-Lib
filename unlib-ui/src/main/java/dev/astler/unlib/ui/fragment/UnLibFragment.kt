package dev.astler.unlib.ui.fragment

import android.content.Context
import androidx.fragment.app.Fragment
import dev.astler.unlib.interfaces.ActivityInterface
import dev.astler.unlib.interfaces.CoreFragmentInterface
import dev.astler.unlib.utils.getStringResource

abstract class UnLibFragment(pLayoutId: Int = 0) : Fragment(pLayoutId), CoreFragmentInterface {

    lateinit var coreListener: ActivityInterface

    override fun onAttach(pContext: Context) {
        super.onAttach(pContext)

        if (pContext is ActivityInterface)
            coreListener = pContext
    }

    override fun backPressed() {
        coreListener.backPressed()
    }

    override fun onResume() {

        super.onResume()
        coreListener.setCurrentFragment(this)
    }

    fun getStringByName(pName: String): String {
        return requireContext().getStringResource(pName)
    }
}
