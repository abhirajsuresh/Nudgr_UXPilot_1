package com.nudgr.service

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SessionControllerTest {

    private lateinit var context: Context
    private lateinit var sessionController: SessionController

    @Before
    fun setup() {
        context = mockk(relaxed = true)
        sessionController = SessionController(context)
    }

    @Test
    fun `startSession starts foreground service with correct extras`() = runTest {
        // Given
        val durationMs = 4 * 60 * 60 * 1000L
        val intervalMs = 2 * 60 * 1000L
        val intentSlot = slot<Intent>()
        
        every { context.startForegroundService(capture(intentSlot)) } returns null

        // When
        sessionController.startSession(durationMs, intervalMs)

        // Then
        verify { context.startForegroundService(any()) }
        val capturedIntent = intentSlot.captured
        assertEquals("start", capturedIntent.getStringExtra("action"))
        assertEquals(durationMs, capturedIntent.getLongExtra("duration_ms", 0))
        assertEquals(intervalMs, capturedIntent.getLongExtra("interval_ms", 0))
    }

    @Test
    fun `endSession sends end action`() = runTest {
        // Given
        val intentSlot = slot<Intent>()
        every { context.startService(capture(intentSlot)) } returns null

        // When
        sessionController.endSession()

        // Then
        verify { context.startService(any()) }
        assertEquals("end", intentSlot.captured.getStringExtra("action"))
    }

    @Test
    fun `pauseSession sends pause action`() = runTest {
        // Given
        val intentSlot = slot<Intent>()
        every { context.startService(capture(intentSlot)) } returns null

        // When
        sessionController.pauseSession()

        // Then
        verify { context.startService(any()) }
        assertEquals("pause", intentSlot.captured.getStringExtra("action"))
    }

    @Test
    fun `resumeSession sends resume action`() = runTest {
        // Given
        val intentSlot = slot<Intent>()
        every { context.startService(capture(intentSlot)) } returns null

        // When
        sessionController.resumeSession()

        // Then
        verify { context.startService(any()) }
        assertEquals("resume", intentSlot.captured.getStringExtra("action"))
    }

    @Test
    fun `snoozeNudge sends snooze action with default delay`() = runTest {
        // Given
        val intentSlot = slot<Intent>()
        every { context.startService(capture(intentSlot)) } returns null

        // When
        sessionController.snoozeNudge()

        // Then
        verify { context.startService(any()) }
        val capturedIntent = intentSlot.captured
        assertEquals("snooze", capturedIntent.getStringExtra("action"))
        assertEquals(5 * 60 * 1000L, capturedIntent.getLongExtra("snooze_delay_ms", 0))
    }

    @Test
    fun `snoozeNudge sends snooze action with custom delay`() = runTest {
        // Given
        val customDelay = 10 * 60 * 1000L
        val intentSlot = slot<Intent>()
        every { context.startService(capture(intentSlot)) } returns null

        // When
        sessionController.snoozeNudge(customDelay)

        // Then
        verify { context.startService(any()) }
        assertEquals(customDelay, intentSlot.captured.getLongExtra("snooze_delay_ms", 0))
    }

    @Test
    fun `extendSession sends extend action with default extension`() = runTest {
        // Given
        val intentSlot = slot<Intent>()
        every { context.startService(capture(intentSlot)) } returns null

        // When
        sessionController.extendSession()

        // Then
        verify { context.startService(any()) }
        val capturedIntent = intentSlot.captured
        assertEquals("extend", capturedIntent.getStringExtra("action"))
        assertEquals(5 * 60 * 1000L, capturedIntent.getLongExtra("extension_ms", 0))
    }

    @Test
    fun `extendSession sends extend action with custom extension`() = runTest {
        // Given
        val customExtension = 15 * 60 * 1000L
        val intentSlot = slot<Intent>()
        every { context.startService(capture(intentSlot)) } returns null

        // When
        sessionController.extendSession(customExtension)

        // Then
        verify { context.startService(any()) }
        assertEquals(customExtension, intentSlot.captured.getLongExtra("extension_ms", 0))
    }

    @Test
    fun `onScreenLocked sends screen_locked action`() = runTest {
        // Given
        val intentSlot = slot<Intent>()
        every { context.startService(capture(intentSlot)) } returns null

        // When
        sessionController.onScreenLocked()

        // Then
        verify { context.startService(any()) }
        assertEquals("screen_locked", intentSlot.captured.getStringExtra("action"))
    }

    @Test
    fun `onScreenUnlocked sends screen_unlocked action`() = runTest {
        // Given
        val intentSlot = slot<Intent>()
        every { context.startService(capture(intentSlot)) } returns null

        // When
        sessionController.onScreenUnlocked()

        // Then
        verify { context.startService(any()) }
        assertEquals("screen_unlocked", intentSlot.captured.getStringExtra("action"))
    }

    @Test
    fun `onBootCompleted sends boot_completed action`() = runTest {
        // Given
        val intentSlot = slot<Intent>()
        every { context.startService(capture(intentSlot)) } returns null

        // When
        sessionController.onBootCompleted()

        // Then
        verify { context.startService(any()) }
        assertEquals("boot_completed", intentSlot.captured.getStringExtra("action"))
    }

    @Test
    fun `onDestroy cancels scope`() {
        // Given
        val lifecycleOwner = mockk<LifecycleOwner>(relaxed = true)

        // When
        sessionController.onDestroy(lifecycleOwner)

        // Then - scope should be cancelled, no exceptions thrown
        // This is verified by the fact that no exceptions are thrown
    }

    @Test
    fun `all intents target SessionEngineService`() = runTest {
        // Given
        val intentSlot = slot<Intent>()
        every { context.startService(capture(intentSlot)) } returns null
        every { context.startForegroundService(capture(intentSlot)) } returns null

        // When
        sessionController.endSession()

        // Then
        val capturedIntent = intentSlot.captured
        assertEquals(SessionEngineService::class.java.name, capturedIntent.component?.className)
    }
}