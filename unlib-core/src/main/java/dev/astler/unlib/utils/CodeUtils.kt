package dev.astler.unlib.utils

import android.content.Context
import dev.astler.unlib.core.R

fun simpleTry(pContext: Context? = null, pAction: () -> Unit) {
    try {
        pAction()
    } catch (pException: Exception) {
        pException.printStackTrace()
        infoLog("Exception! ${pException.message}")
        pContext?.makeToast(R.string.something_went_wrong)
    }
}

fun simpleTryCatch(pContext: Context? = null, pAction: () -> Unit, pCatch: () -> Unit) {
    try {
        pAction()
    } catch (pException: Exception) {
        pException.printStackTrace()
        infoLog("Exception! ${pException.message}")
        pContext?.makeToast(R.string.something_went_wrong)
    }
}