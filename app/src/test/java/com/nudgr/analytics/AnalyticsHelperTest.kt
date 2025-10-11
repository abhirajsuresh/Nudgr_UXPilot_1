package com.nudgr.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AnalyticsHelperTest {

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var analyticsHelper: AnalyticsHelper

    @Before
    fun setup() {
        firebaseAnalytics = mockk(relaxed = true)
        analyticsHelper = AnalyticsHelper(firebaseAnalytics)
    }

    @Test
    fun `trackEvent calls firebase with event name`() {
        // Given
        val eventName = "test_event"
        val eventSlot = slot<String>()
        val bundleSlot = slot<Bundle>()
        
        every { 
            firebaseAnalytics.logEvent(capture(eventSlot), capture(bundleSlot)) 
        } returns Unit

        // When
        analyticsHelper.trackEvent(eventName)

        // Then
        verify { firebaseAnalytics.logEvent(eventName, any()) }
        assertEquals(eventName, eventSlot.captured)
    }

    @Test
    fun `trackEvent calls firebase with event name and params`() {
        // Given
        val eventName = "test_event"
        val params = Bundle().apply {
            putString("key", "value")
            putInt("count", 42)
        }
        
        every { firebaseAnalytics.logEvent(any(), any()) } returns Unit

        // When
        analyticsHelper.trackEvent(eventName, params)

        // Then
        verify { firebaseAnalytics.logEvent(eventName, params) }
    }

    @Test
    fun `Event constants are defined correctly`() {
        assertEquals("onboarding_guest_continue", AnalyticsHelper.Event.ONBOARDING_GUEST_CONTINUE)
        assertEquals("onboarding_checklist_complete", AnalyticsHelper.Event.ONBOARDING_CHECKLIST_COMPLETE)
        assertEquals("session_start", AnalyticsHelper.Event.SESSION_START)
        assertEquals("session_complete", AnalyticsHelper.Event.SESSION_COMPLETE)
        assertEquals("session_end_manual", AnalyticsHelper.Event.SESSION_END_MANUAL)
        assertEquals("session_pause", AnalyticsHelper.Event.SESSION_PAUSE)
        assertEquals("session_resume", AnalyticsHelper.Event.SESSION_RESUME)
        assertEquals("nudge_shown", AnalyticsHelper.Event.NUDGE_SHOWN)
        assertEquals("images_added", AnalyticsHelper.Event.IMAGES_ADDED)
        assertEquals("images_deleted", AnalyticsHelper.Event.IMAGES_DELETED)
    }

    @Test
    fun `Param constants are defined correctly`() {
        assertEquals("duration_planned_min", AnalyticsHelper.Param.DURATION_PLANNED_MIN)
        assertEquals("duration_actual_sec", AnalyticsHelper.Param.DURATION_ACTUAL_SEC)
        assertEquals("interval_sec", AnalyticsHelper.Param.INTERVAL_SEC)
        assertEquals("image_count", AnalyticsHelper.Param.IMAGE_COUNT)
    }

    @Test
    fun `trackEvent with empty bundle works`() {
        // Given
        val eventName = "empty_event"
        val emptyBundle = Bundle()
        
        every { firebaseAnalytics.logEvent(any(), any()) } returns Unit

        // When
        analyticsHelper.trackEvent(eventName, emptyBundle)

        // Then
        verify { firebaseAnalytics.logEvent(eventName, emptyBundle) }
    }

    @Test
    fun `trackEvent with multiple params works`() {
        // Given
        val eventName = "complex_event"
        val params = Bundle().apply {
            putInt(AnalyticsHelper.Param.IMAGE_COUNT, 5)
            putInt(AnalyticsHelper.Param.DURATION_PLANNED_MIN, 240)
            putInt(AnalyticsHelper.Param.DURATION_ACTUAL_SEC, 14000)
        }
        
        every { firebaseAnalytics.logEvent(any(), any()) } returns Unit

        // When
        analyticsHelper.trackEvent(eventName, params)

        // Then
        verify { firebaseAnalytics.logEvent(eventName, params) }
    }
}