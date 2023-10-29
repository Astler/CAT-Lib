package dev.astler.unlib_test.analytics

import dev.astler.catlib.analytics.CatAnalytics


class CatTestAnalytics {
  companion object {
      fun trackClick(clickInfo: String) {
          CatAnalytics.truckCustomEvent("click", Pair("info", clickInfo))
      }

      fun missingAssetsResource(path: String) {
          CatAnalytics.truckCustomEvent("missing_asset", Pair("path", path))
      }
  }
}