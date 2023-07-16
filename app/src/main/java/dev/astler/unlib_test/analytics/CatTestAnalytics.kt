package dev.astler.unlib_test.analytics

import dev.astler.catlib.analytics.CatAnalytics

class CatTestAnalytics: CatAnalytics() {
    fun trackClick(clickInfo: String) {
        simpleEvent("click", Pair("info", clickInfo))
    }
}