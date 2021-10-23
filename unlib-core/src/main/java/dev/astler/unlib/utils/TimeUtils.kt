package dev.astler.unlib.utils

import dev.astler.unlib.gPreferencesTool
import java.util.* // ktlint-disable no-wildcard-imports

fun String.hasPrefsTimePassed(millisTimeToCheck: Long): Boolean {
    return GregorianCalendar().timeInMillis - gPreferencesTool.getLong(
        this,
        GregorianCalendar().timeInMillis
    ) >= millisTimeToCheck
}
