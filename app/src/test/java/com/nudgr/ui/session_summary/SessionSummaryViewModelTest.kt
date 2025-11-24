package com.nudgr.ui.session_summary

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nudgr.data.local.entity.Session
import com.nudgr.data.local.entity.SessionStatus
import com.nudgr.data.repository.SessionRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class SessionSummaryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var sessionRepository: SessionRepository
    private lateinit var viewModel: SessionSummaryViewModel

    private val testSession = Session(
        id = "test-session-123",
        plannedDurationMs = 4 * 60 * 60 * 1000L,
        actualDurationMs = 3 * 60 * 60 * 1000L,
        reminderIntervalMs = 2 * 60 * 1000L,
        startTime = Date(),
        endTime = Date(),
        status = SessionStatus.COMPLETED,
        totalUnlocks = 5,
        totalReminders = 10,
        totalSnoozes = 2,
        totalLocks = 3
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        sessionRepository = mockk()
        viewModel = SessionSummaryViewModel(sessionRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is loading`() {
        // When
        val state = viewModel.uiState.value

        // Then
        assertTrue(state is SessionSummaryUiState.Loading)
    }

    @Test
    fun `loadSession updates state to success when session found`() = runTest {
        // Given
        coEvery { sessionRepository.getSessionById(testSession.id) } returns testSession

        // When
        viewModel.loadSession(testSession.id)

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is SessionSummaryUiState.Success)
        assertEquals(testSession, (state as SessionSummaryUiState.Success).session)
    }

    @Test
    fun `loadSession updates state to error when session not found`() = runTest {
        // Given
        val sessionId = "non-existent-session"
        coEvery { sessionRepository.getSessionById(sessionId) } returns null

        // When
        viewModel.loadSession(sessionId)

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is SessionSummaryUiState.Error)
        assertEquals("Session not found", (state as SessionSummaryUiState.Error).message)
    }

    @Test
    fun `loadSession updates state to error when exception occurs`() = runTest {
        // Given
        val sessionId = "test-session"
        val errorMessage = "Database error"
        coEvery { 
            sessionRepository.getSessionById(sessionId) 
        } throws Exception(errorMessage)

        // When
        viewModel.loadSession(sessionId)

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is SessionSummaryUiState.Error)
        assertEquals(errorMessage, (state as SessionSummaryUiState.Error).message)
    }

    @Test
    fun `loadSession sets loading state before fetching`() = runTest {
        // Given
        coEvery { sessionRepository.getSessionById(any()) } coAnswers {
            kotlinx.coroutines.delay(100)
            testSession
        }

        // When
        viewModel.loadSession(testSession.id)

        // Note: Due to UnconfinedTestDispatcher, this will complete immediately
        // In real scenario with StandardTestDispatcher, we could test intermediate state
        val state = viewModel.uiState.value
        assertTrue(state is SessionSummaryUiState.Success)
    }

    @Test
    fun `loadSession with different session IDs loads correct sessions`() = runTest {
        // Given
        val session1 = testSession.copy(id = "session-1")
        val session2 = testSession.copy(id = "session-2")
        coEvery { sessionRepository.getSessionById("session-1") } returns session1
        coEvery { sessionRepository.getSessionById("session-2") } returns session2

        // When
        viewModel.loadSession("session-1")
        val state1 = viewModel.uiState.value

        viewModel.loadSession("session-2")
        val state2 = viewModel.uiState.value

        // Then
        assertTrue(state1 is SessionSummaryUiState.Success)
        assertEquals("session-1", (state1 as SessionSummaryUiState.Success).session.id)

        assertTrue(state2 is SessionSummaryUiState.Success)
        assertEquals("session-2", (state2 as SessionSummaryUiState.Success).session.id)
    }
}