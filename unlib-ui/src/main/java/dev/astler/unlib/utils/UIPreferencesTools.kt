package dev.astler.unlib.utils

import dev.astler.unlib.PreferencesTool
import dev.astler.unlib.cResumeTime

var PreferencesTool.appResumeTime: Long
    get() = getLong(cResumeTime, 0L)
    set(value) {
        edit(cResumeTime, value)
    }
