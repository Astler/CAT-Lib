package dev.astler.catlib.extensions

/**
 * Own default fallback value for Map as replacement for Map.getOrDefault, cuz it's only available from API 24
 */
fun <K, V> Map<K, V>.getValueOrDefault(key: K, default: V): V {
    val nValue = if (containsKey(key)) {
        get(key)
    } else {
        default
    }

    return nValue ?: default
}
