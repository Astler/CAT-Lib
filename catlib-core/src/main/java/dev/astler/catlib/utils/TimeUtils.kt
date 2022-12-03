package dev.astler.catlib.utils

import dev.astler.catlib.gPreferencesTool
import java.util.* // ktlint-disable no-wildcard-imports

fun String.hasPrefsTimePassed(millisTimeToCheck: Long, default: Long = GregorianCalendar().timeInMillis): Boolean {
    return GregorianCalendar().timeInMillis - gPreferencesTool.getLong(
        this, default
    ) >= millisTimeToCheck
}
