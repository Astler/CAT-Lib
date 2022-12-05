package dev.astler.cat_ui.utils.views

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

fun Fragment.setClickableViewForSafeNavigation(pView: View, pCurrentFragmentIdCheck: Int, pDirections: NavDirections) {
    pView.setOnClickListener {
        safeNavigation(pCurrentFragmentIdCheck, pDirections)
    }
}

fun Fragment.safeNavigation(pCurrentFragmentIdCheck: Int, pDirections: NavDirections) {
    lifecycleScope.launchWhenResumed {
        val nNavController = findNavController()

        if (nNavController.currentDestination?.id == pCurrentFragmentIdCheck)
            nNavController.navigate(pDirections)
    }
}

fun Fragment.safeGlobalNavigation(pDirections: NavDirections) {
    lifecycleScope.launchWhenResumed {
        findNavController().navigate(pDirections)
    }
}
