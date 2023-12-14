package dev.astler.catlib.helpers

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

/**
 * try-catch-finally extensions with ability to use default return value and errors tracking
 */

fun trackedTry(errorCatchAction: ((Exception) -> Unit)? = null, finallyAction: (() -> Unit)? = null, action: (() -> Unit)? = null) {
    try {
        action?.invoke()
    } catch (e: Exception) {
        errorCatchAction?.invoke(e)
        Firebase.crashlytics.recordException(e)
        errorLog("Exception START! ${e.message}\n\n${e.stackTraceToString()}\n\n${e.cause}\n\nException END!")
    } finally {
        finallyAction?.invoke()
    }
}

fun <T> trackedTry(
    errorCatchAction: ((Exception) -> T)? = null,
    fallbackValue: T, finallyAction: (() -> Unit)? = null,
    action: (() -> T)? = null
): T {
    return try {
        action?.invoke() ?: fallbackValue
    } catch (e: Exception) {
        Firebase.crashlytics.recordException(e)
        errorLog("Exception START! ${e.message}\n\n${e.stackTraceToString()}\n\n${e.cause}\n\nException END!")
        errorCatchAction?.invoke(e) ?: fallbackValue
    } finally {
        finallyAction?.invoke() ?: fallbackValue
    }
}
