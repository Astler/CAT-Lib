package dev.astler.catlib.utils

import dev.astler.catlib.gPreferencesTool
import java.util.* 

fun String.hasPrefsTimePassed(millisTimeToCheck: Long, default: Long = GregorianCalendar().timeInMillis): Boolean {
    return GregorianCalendar().timeInMillis - gPreferencesTool.getLong(
        this, default
    ) >= millisTimeToCheck
}
