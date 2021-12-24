package dev.astler.cat_ui

import dev.astler.unlib.PreferencesTool

var PreferencesTool.appResumeTime: Long
    get() = getLong(cResumeTime, 0L)
    set(value) {
        edit(cResumeTime, value)
    }
