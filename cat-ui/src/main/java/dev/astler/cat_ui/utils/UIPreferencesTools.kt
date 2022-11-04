package dev.astler.cat_ui.utils

import dev.astler.cat_ui.ResumeTimeKey
import dev.astler.unlib.PreferencesTool

var PreferencesTool.appResumeTime: Long
    get() = getLong(ResumeTimeKey, 0L)
    set(value) {
        edit(ResumeTimeKey, value)
    }
