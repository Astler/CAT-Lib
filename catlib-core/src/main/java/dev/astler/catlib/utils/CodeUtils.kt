package dev.astler.catlib.utils

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

fun <K, V> Map<K, V>.getValueOrDefault(key: K, default: V): V {
    val nValue = if (containsKey(key)) {
        get(key)
    } else {
        default
    }

    return nValue ?: default
}

fun trySimple(pAction: () -> Unit) {
    try {
        pAction()
    } catch (e: Exception) {
        Firebase.crashlytics.recordException(e)
        errorLog("Exception! ${e.message}")
    }
}

fun tryFinally(pAction: () -> Unit, pFinally: () -> Unit) {
    try {
        pAction()
    } catch (e: Exception) {
        Firebase.crashlytics.recordException(e)
        errorLog("Exception! ${e.message}")
    } finally {
        pFinally()
    }
}

fun <B, T : Any> tryWithParameters(
    pTryParameter: B,
    pCatchParameter: B,
    pAction: (B) -> T
): T? {
    return try {
        pAction(pTryParameter)
    } catch (e: Exception) {
        Firebase.crashlytics.recordException(e)
        errorLog("Exception! ${e.message}")
        pAction(pCatchParameter)
    }
}

fun <T> tryWithNullDefault(
    pFallBack: T? = null,
    pAction: () -> T
): T? {
    return try {
        pAction()
    } catch (e: Exception) {
        Firebase.crashlytics.recordException(e)
        errorLog("Exception! ${e.message}")
        pFallBack
    }
}

fun <T> tryWithDefault(
    pFallBack: T,
    pAction: () -> T
): T {
    return try {
        pAction()
    } catch (e: Exception) {
        Firebase.crashlytics.recordException(e)
        errorLog("Exception! ${e.message}")
        pFallBack
    }
}
