package dev.astler.cat_ui.extensions

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.play.core.review.ReviewException
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import dev.astler.cat_ui.activities.CatActivity
import dev.astler.cat_ui.appResumeTime
import dev.astler.catlib.extensions.isPlayStoreInstalled
import dev.astler.catlib.helpers.errorLog
import dev.astler.catlib.helpers.infoLog
import kotlinx.coroutines.launch
import java.util.GregorianCalendar

class InAppReviewActivityExtension(private val _catActivity: CatActivity, snapParentView: View) {

    private val _preferences = _catActivity.preferences
    private var _reviewInfo: ReviewInfo? = null
    private val _reviewManager: ReviewManager by lazy {
        ReviewManagerFactory.create(_catActivity)
    }

    init {
        infoLog("InAppReviewActivityExtension: init")

        with(GoogleApiAvailability.getInstance()) {
            if (_catActivity.isPlayStoreInstalled()) {
                try {
                    infoLog("InAppReviewActivityExtension: requestReviewFlow")
                    _reviewManager.requestReviewFlow().addOnCompleteListener { request ->
                        if (request.isSuccessful) {
                            _reviewInfo = request.result
                        } else {
                            errorLog(request.exception, "error during requestReviewFlow")
                        }
                    }

                } catch (e: ReviewException) {
                    errorLog(e, "error during requestReviewFlow")
                }
            }
        }

        _catActivity.onFragmentChangedListener = {
            tryToShow()
        }
    }

    private fun tryToShow() {
        val timeFromResume =
            GregorianCalendar().timeInMillis - _preferences.appResumeTime

        infoLog("InAppReviewActivityExtension: Try to show review dialog: $timeFromResume ${timeFromResume < 200000}")

        if (timeFromResume < 200000) return

        _reviewInfo?.let { it1 ->
            _reviewManager.launchReviewFlow(_catActivity, it1)
            _preferences.appResumeTime = GregorianCalendar().timeInMillis
        }
    }
}