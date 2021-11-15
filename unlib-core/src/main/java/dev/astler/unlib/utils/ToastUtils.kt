package dev.astler.unlib.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dev.astler.unlib.core.R

var globalToast: Toast? = null
const val cToastDefaultText = "Toast"

fun Context.toast(
    pToastText: CharSequence = cToastDefaultText,
    pToastLength: Int = Toast.LENGTH_SHORT,
    pShowToast: Boolean = true
) {
    globalToast?.cancel()

    val nToast = Toast.makeText(
        this,
        pToastText,
        pToastLength
    )

    if (pShowToast)
        nToast.show()

    globalToast = nToast
}

fun Context.toast(
    pToastRes: Int = R.string.app_name,
    pToastLength: Int = Toast.LENGTH_SHORT,
    pShowToast: Boolean = true
) {
    toast(resources.getText(pToastRes), pToastLength, pShowToast)
}

fun Fragment.safeToast(
    pToastRes: Int = R.string.app_name,
    pToastLength: Int = Toast.LENGTH_SHORT,
    pShowToast: Boolean = true
) {
    lifecycleScope.launchWhenResumed {
        requireContext().toast(pToastRes, pToastLength, pShowToast)
    }
}

fun Fragment.safeToast(
    pToastText: CharSequence = cToastDefaultText,
    pToastLength: Int = Toast.LENGTH_SHORT,
    pShowToast: Boolean = true
) {
    lifecycleScope.launchWhenResumed {
        requireContext().toast(pToastText, pToastLength, pShowToast)
    }
}

fun AppCompatActivity.safeToast(
    pToastRes: Int = R.string.app_name,
    pToastLength: Int = Toast.LENGTH_SHORT,
    pShowToast: Boolean = true
) {
    lifecycleScope.launchWhenResumed {
        toast(pToastRes, pToastLength, pShowToast)
    }
}

fun AppCompatActivity.safeToast(
    pToastText: CharSequence = cToastDefaultText,
    pToastLength: Int = Toast.LENGTH_SHORT,
    pShowToast: Boolean = true
) {
    lifecycleScope.launchWhenResumed {
        toast(pToastText, pToastLength, pShowToast)
    }
}
