package dev.astler.unlib.utils

import android.content.Context
import dev.astler.unlib.core.R

fun trySimple(pAction: () -> Unit) {
    try {
        pAction()
    } catch (pException: Exception) {
        errorLog("Exception! ${pException.message}")
    }
}

fun <B, T : Any> tryWithParameters(
    pTryParameter: B,
    pCatchParameter: B,
    pAction: (B) -> T
): T? {
    return try {
        pAction(pTryParameter)
    } catch (pException: Exception) {
        errorLog("Exception! ${pException.message}")
        pAction(pCatchParameter)
    }
}

fun <T> tryWithDefault(
    pContext: Context? = null,
    pFallBack: T? = null,
    pAction: () -> T
): T? {
    return try {
        pAction()
    } catch (pException: Exception) {
        pException.printStackTrace()
        errorLog("Exception! ${pException.message}")
        pContext?.toast(R.string.something_went_wrong)
        pFallBack
    }
}
