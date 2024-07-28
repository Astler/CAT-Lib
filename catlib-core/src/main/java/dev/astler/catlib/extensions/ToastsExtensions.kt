package dev.astler.catlib.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

var globalToast: Toast? = null
const val cToastDefaultText = "Toast"

fun Context.toast(
    pToastText: CharSequence = cToastDefaultText,
    pToastLength: Int = Toast.LENGTH_SHORT,
    pShowToast: Boolean = true
): Toast? {
    globalToast?.cancel()

    val nToast = Toast.makeText(
        this,
        pToastText,
        pToastLength
    )

    if (pShowToast)
        nToast.show()

    globalToast = nToast

    return nToast
}

fun Context.toast(@StringRes textId: Int, length: Int = Toast.LENGTH_SHORT, instantShow: Boolean = true
): Toast? {
    return toast(resources.getText(textId), length, instantShow)
}