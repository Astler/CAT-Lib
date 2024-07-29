package dev.astler.ui

import dev.astler.catlib.preferences.PreferencesTool

var PreferencesTool.appResumeTime: Long
    get() = getLong(ResumeTimeKey, 0L)
    set(value) {
        edit(ResumeTimeKey, value)
    }
