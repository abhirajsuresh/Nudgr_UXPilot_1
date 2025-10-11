package com.nudgr.ui.dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nudgr.analytics.AnalyticsHelper
import com.nudgr.data.local.entity.Session
import com.nudgr.data.local.entity.SessionStatus
import com.nudgr.data.repository.ImageRepository
import com.nudgr.data.repository.SessionRepository
import com.nudgr.data.repository.SettingsRepository
import com.nudgr.service.SessionController
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var analyticsHelper: AnalyticsHelper
    private lateinit var imageRepository: ImageRepository
    private lateinit var sessionRepository: SessionRepository
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var sessionController: SessionController
    private lateinit var viewModel: DashboardViewModel

    private val testSession = Session(
        id = "test-session",
        plannedDurationMs = 4 * 60 * 60 * 1000L,
        actualDurationMs = 0L,
        reminderIntervalMs = 2 * 60 * 1000L,
        startTime = Date(),
        endTime = null,
        status = SessionStatus.RUNNING
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        analyticsHelper = mockk(relaxed = true)
        imageRepository = mockk()
        sessionRepository = mockk()
        settingsRepository = mockk()
        sessionController = mockk(relaxed = true)
        
        viewModel = DashboardViewModel(
            analyticsHelper,
            imageRepository,
            sessionRepository,
            settingsRepository,
            sessionController
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has default values`() {
        // When
        val state = viewModel.uiState.value

        // Then
        assertEquals(0, state.imageCount)
        assertEquals(0, state.totalSessions)
        assertNull(state.lastSession)
        assertEquals(SessionStatus.OFF, state.sessionStatus)
        assertFalse(state.canStartSession)
    }

    @Test
    fun `loadDashboardData updates image count`() = runTest {
        // Given
        coEvery { imageRepository.getActiveImageCount() } returns 5
        coEvery { sessionRepository.getCurrentSession() } returns null
        every { sessionRepository.getAllSessions() } returns flowOf(emptyList())

        // When
        viewModel.loadDashboardData()

        // Then
        val state = viewModel.uiState.value
        assertEquals(5, state.imageCount)
    }

    @Test
    fun `loadDashboardData updates session status when session running`() = runTest {
        // Given
        coEvery { imageRepository.getActiveImageCount() } returns 5
        coEvery { sessionRepository.getCurrentSession() } returns testSession
        every { sessionRepository.getAllSessions() } returns flowOf(listOf(testSession))

        // When
        viewModel.loadDashboardData()

        // Then
        val state = viewModel.uiState.value
        assertEquals(SessionStatus.RUNNING, state.sessionStatus)
    }

    @Test
    fun `loadDashboardData sets canStartSession true when conditions met`() = runTest {
        // Given
        coEvery { imageRepository.getActiveImageCount() } returns 5
        coEvery { sessionRepository.getCurrentSession() } returns null
        every { sessionRepository.getAllSessions() } returns flowOf(emptyList())

        // When
        viewModel.loadDashboardData()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state.canStartSession)
    }

    @Test
    fun `loadDashboardData sets canStartSession false when no images`() = runTest {
        // Given
        coEvery { imageRepository.getActiveImageCount() } returns 0
        coEvery { sessionRepository.getCurrentSession() } returns null
        every { sessionRepository.getAllSessions() } returns flowOf(emptyList())

        // When
        viewModel.loadDashboardData()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.canStartSession)
    }

    @Test
    fun `loadDashboardData sets canStartSession false when session active`() = runTest {
        // Given
        coEvery { imageRepository.getActiveImageCount() } returns 5
        coEvery { sessionRepository.getCurrentSession() } returns testSession
        every { sessionRepository.getAllSessions() } returns flowOf(listOf(testSession))

        // When
        viewModel.loadDashboardData()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.canStartSession)
    }

    @Test
    fun `loadDashboardData updates total sessions count`() = runTest {
        // Given
        val sessions = listOf(
            testSession.copy(id = "1", status = SessionStatus.COMPLETED),
            testSession.copy(id = "2", status = SessionStatus.COMPLETED),
            testSession.copy(id = "3", status = SessionStatus.COMPLETED)
        )
        coEvery { imageRepository.getActiveImageCount() } returns 5
        coEvery { sessionRepository.getCurrentSession() } returns null
        every { sessionRepository.getAllSessions() } returns flowOf(sessions)

        // When
        viewModel.loadDashboardData()

        // Then
        val state = viewModel.uiState.value
        assertEquals(3, state.totalSessions)
    }

    @Test
    fun `startSession calls controller and tracks analytics`() = runTest {
        // Given
        val durationMs = 4 * 60 * 60 * 1000L
        val intervalMs = 2 * 60 * 1000L
        every { settingsRepository.durationMs } returns flowOf(durationMs)
        every { settingsRepository.intervalMs } returns flowOf(intervalMs)
        coEvery { imageRepository.getActiveImageCount() } returns 5
        coEvery { sessionRepository.getCurrentSession() } returns null
        every { sessionRepository.getAllSessions() } returns flowOf(emptyList())

        // When
        viewModel.startSession()

        // Then
        verify { sessionController.startSession(durationMs, intervalMs) }
        verify { analyticsHelper.trackEvent(AnalyticsHelper.Event.SESSION_START) }
    }

    @Test
    fun `endSession calls controller and tracks analytics`() = runTest {
        // Given
        coEvery { imageRepository.getActiveImageCount() } returns 5
        coEvery { sessionRepository.getCurrentSession() } returns null
        every { sessionRepository.getAllSessions() } returns flowOf(emptyList())

        // When
        viewModel.endSession()

        // Then
        verify { sessionController.endSession() }
        verify { analyticsHelper.trackEvent(AnalyticsHelper.Event.SESSION_END_MANUAL) }
    }

    @Test
    fun `togglePause pauses when session running`() = runTest {
        // Given
        coEvery { imageRepository.getActiveImageCount() } returns 5
        coEvery { sessionRepository.getCurrentSession() } returns testSession.copy(status = SessionStatus.RUNNING)
        every { sessionRepository.getAllSessions() } returns flowOf(listOf(testSession))
        viewModel.loadDashboardData()

        // When
        viewModel.togglePause()

        // Then
        verify { sessionController.pauseSession() }
        verify { analyticsHelper.trackEvent(AnalyticsHelper.Event.SESSION_PAUSE) }
    }

    @Test
    fun `togglePause resumes when session paused`() = runTest {
        // Given
        coEvery { imageRepository.getActiveImageCount() } returns 5
        coEvery { sessionRepository.getCurrentSession() } returns testSession.copy(status = SessionStatus.PAUSED)
        every { sessionRepository.getAllSessions() } returns flowOf(listOf(testSession))
        viewModel.loadDashboardData()

        // When
        viewModel.togglePause()

        // Then
        verify { sessionController.resumeSession() }
        verify { analyticsHelper.trackEvent(AnalyticsHelper.Event.SESSION_RESUME) }
    }

    @Test
    fun `togglePause does nothing when session off`() = runTest {
        // Given
        coEvery { imageRepository.getActiveImageCount() } returns 5
        coEvery { sessionRepository.getCurrentSession() } returns null
        every { sessionRepository.getAllSessions() } returns flowOf(emptyList())
        viewModel.loadDashboardData()

        // When
        viewModel.togglePause()

        // Then
        verify(exactly = 0) { sessionController.pauseSession() }
        verify(exactly = 0) { sessionController.resumeSession() }
    }

    @Test
    fun `loadDashboardData finds last completed session`() = runTest {
        // Given
        val completedSession = testSession.copy(
            id = "completed",
            status = SessionStatus.COMPLETED,
            endTime = Date()
        )
        val sessions = listOf(
            testSession.copy(id = "1", status = SessionStatus.RUNNING),
            completedSession,
            testSession.copy(id = "3", status = SessionStatus.CANCELLED)
        )
        coEvery { imageRepository.getActiveImageCount() } returns 5
        coEvery { sessionRepository.getCurrentSession() } returns null
        every { sessionRepository.getAllSessions() } returns flowOf(sessions)

        // When
        viewModel.loadDashboardData()

        // Then
        val state = viewModel.uiState.value
        assertEquals(completedSession, state.lastSession)
    }
}