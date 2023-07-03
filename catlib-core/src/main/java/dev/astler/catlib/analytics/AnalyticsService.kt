package dev.astler.catlib.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class AnalyticsService {

    private var firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    fun missingAssetsResource(path: String) {
        simpleEvent("missing_asset", Pair("path", path))
    }

    private fun simpleEvent(key: String, vararg params: Pair<String, Any>) {
        val bundle: Bundle? = if (params.isNotEmpty()) {
            Bundle().also {
                params.forEach { pair ->
                    it.putString(pair.first, pair.second.toString())
                }
            }
        } else null

        firebaseAnalytics.logEvent(key, bundle)
    }

}