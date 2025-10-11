package com.nudgr.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsHelper @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) {

    fun trackEvent(eventName: String, params: Bundle = Bundle()) {
        firebaseAnalytics.logEvent(eventName, params)
    }

    object Event {
        const val ONBOARDING_GUEST_CONTINUE = "onboarding_guest_continue"
        const val ONBOARDING_CHECKLIST_COMPLETE = "onboarding_checklist_complete"
        const val SESSION_START = "session_start"
        const val SESSION_COMPLETE = "session_complete"
        const val SESSION_END_MANUAL = "session_end_manual"
        const val SESSION_PAUSE = "session_pause"
        const val SESSION_RESUME = "session_resume"
        const val NUDGE_SHOWN = "nudge_shown"
        const val IMAGES_ADDED = "images_added"
        const val IMAGES_DELETED = "images_deleted"
    }

    object Param {
        const val DURATION_PLANNED_MIN = "duration_planned_min"
        const val DURATION_ACTUAL_SEC = "duration_actual_sec"
        const val INTERVAL_SEC = "interval_sec"
        const val IMAGE_COUNT = "image_count"
    }
}
