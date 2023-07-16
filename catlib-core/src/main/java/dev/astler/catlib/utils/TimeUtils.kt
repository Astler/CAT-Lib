package dev.astler.catlib.utils

import dev.astler.catlib.gPreferencesTool
import java.util.*

fun String.hasPrefsTimePassed(
    millisTimeToCheck: Long,
    default: Long = GregorianCalendar().timeInMillis
): Boolean {
    return GregorianCalendar().timeInMillis - gPreferencesTool.getLong(
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
