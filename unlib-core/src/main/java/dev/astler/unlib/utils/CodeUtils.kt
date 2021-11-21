package dev.astler.unlib.utils

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

fun <T> tryWithNullDefault(
    pFallBack: T? = null,
    pAction: () -> T
): T? {
    return try {
        pAction()
    } catch (pException: Exception) {
        errorLog("Exception! ${pException.message}")
        pFallBack
    }
}

fun <T> tryWithDefault(
    pFallBack: T,
    pAction: () -> T
): T {
    return try {
        pAction()
    } catch (pException: Exception) {
        errorLog("Exception! ${pException.message}")
        pFallBack
    }
}
