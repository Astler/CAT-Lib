package dev.astler.unlib.utils

import dev.astler.unlib.gPreferencesTool
import java.util.* // ktlint-disable no-wildcard-imports

fun String.hasPrefsTimePassed(millisTimeToCheck: Long, default: Long = GregorianCalendar().timeInMillis): Boolean {
    return GregorianCalendar().timeInMillis - gPreferencesTool.getLong(
        this, default
    ) >= millisTimeToCheck
}
