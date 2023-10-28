package dev.astler.catlib.utils

import dev.astler.catlib.preferences.PreferencesTool
import java.util.*

fun String.hasPrefsTimePassed(preferencesTool: PreferencesTool,
    millisTimeToCheck: Long,
    default: Long = GregorianCalendar().timeInMillis
): Boolean {
    return GregorianCalendar().timeInMillis - preferencesTool.getLong(
        this, default
    ) >= millisTimeToCheck
}

fun Long.hasPrefsTimePassed(
    millisTimeToCheck: Long
): Boolean {
    return hasPrefsTimePassed(this, millisTimeToCheck)
}

fun hasPrefsTimePassed(
    currentValue: Long,
    millisTimeToCheck: Long,
    default: Long = GregorianCalendar().timeInMillis
): Boolean {
    return GregorianCalendar().timeInMillis - currentValue >= millisTimeToCheck
}
