package dev.astler.unlib.utils

import android.content.Context
import dev.astler.unlib.core.R

fun simpleTry(pContext: Context? = null, pAction: () -> Unit) {
    try {
        pAction()
    } catch (pException: Exception) {
        pException.printStackTrace()
        errorLog("Exception! ${pException.message}")
        pContext?.makeToast(R.string.something_went_wrong)
    }
}

fun simpleTryCatch(pContext: Context? = null, pAction: () -> Unit, pCatch: () -> Unit) {
    try {
        pAction()
    } catch (pException: Exception) {
        pCatch()
        pException.printStackTrace()
        errorLog("Exception! ${pException.message}")
        pContext?.makeToast(R.string.something_went_wrong)
    }
}

fun <T> typedTry(
    pContext: Context? = null,
    pFallBack: T? = null,
    pAction: () -> T
): T? {
    return try {
        pAction()
    } catch (pException: Exception) {
        pException.printStackTrace()
        errorLog("Exception! ${pException.message}")
        pContext?.makeToast(R.string.something_went_wrong)
        pFallBack
    }
}
