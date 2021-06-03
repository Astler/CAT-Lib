package dev.astler.unlib.utils

import android.content.Context
import android.widget.Toast
import dev.astler.unlib.core.R

fun Context.makeToast(pToastText: String = "Toast", pToastLength: Int = Toast.LENGTH_SHORT, pShowToast: Boolean = true, pToast: Toast? = null): Toast {
    pToast?.cancel()

    val nToast = Toast.makeText(
        this,
        pToastText,
        pToastLength
    )

    if (pShowToast)
        nToast.show()

    return nToast
}

fun Context.makeToast(pToastText: Int = R.string.app_name, pToastLength: Int = Toast.LENGTH_SHORT, pShowToast: Boolean = true, pToast: Toast? = null): Toast {
    pToast?.cancel()

    val nToast = Toast.makeText(
        this,
        pToastText,
        pToastLength
    )

    if (pShowToast)
        nToast.show()

    return nToast
}
