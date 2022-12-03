package dev.astler.cat_ui

import dev.astler.catlib.PreferencesTool

var PreferencesTool.appResumeTime: Long
    get() = getLong(ResumeTimeKey, 0L)
    set(value) {
        edit(ResumeTimeKey, value)
    }
