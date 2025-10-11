package com.nudgr.data.repository

import com.nudgr.data.local.dao.SessionDao
import com.nudgr.data.local.dao.SessionEventDao
import com.nudgr.data.local.entity.Session
import com.nudgr.data.local.entity.SessionEvent
import com.nudgr.data.local.entity.SessionEventType
import com.nudgr.data.local.entity.SessionStatus
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*

class SessionRepositoryTest {

    private lateinit var sessionDao: SessionDao
    private lateinit var sessionEventDao: SessionEventDao
    private lateinit var repository: SessionRepository

    private val testSessionId = "test-session-123"
    private val testSession = Session(
        id = testSessionId,
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
        sessionDao = mockk()
        sessionEventDao = mockk()
        repository = SessionRepository(sessionDao, sessionEventDao)
    }

    @Test
    fun `getAllSessions returns flow from dao`() = runTest {
        // Given
        val sessions = listOf(testSession)
        every { sessionDao.getAllSessions() } returns flowOf(sessions)

        // When
        val result = repository.getAllSessions()

        // Then
        result.collect { sessionList ->
            assertEquals(1, sessionList.size)
            assertEquals(testSession, sessionList[0])
        }
    }

    @Test
    fun `getSessionById returns session when exists`() = runTest {
        // Given
        coEvery { sessionDao.getSessionById(testSessionId) } returns testSession

        // When
        val result = repository.getSessionById(testSessionId)

        // Then
        assertEquals(testSession, result)
        coVerify { sessionDao.getSessionById(testSessionId) }
    }

    @Test
    fun `getSessionById returns null when not exists`() = runTest {
        // Given
        coEvery { sessionDao.getSessionById(testSessionId) } returns null

        // When
        val result = repository.getSessionById(testSessionId)

        // Then
        assertNull(result)
    }

    @Test
    fun `getCurrentSession returns active session`() = runTest {
        // Given
        coEvery { sessionDao.getCurrentSession() } returns testSession

        // When
        val result = repository.getCurrentSession()

        // Then
        assertEquals(testSession, result)
    }

    @Test
    fun `insertSession calls dao insert`() = runTest {
        // Given
        coEvery { sessionDao.insertSession(testSession) } returns Unit

        // When
        repository.insertSession(testSession)

        // Then
        coVerify { sessionDao.insertSession(testSession) }
    }

    @Test
    fun `updateSession calls dao update`() = runTest {
        // Given
        coEvery { sessionDao.updateSession(testSession) } returns Unit

        // When
        repository.updateSession(testSession)

        // Then
        coVerify { sessionDao.updateSession(testSession) }
    }

    @Test
    fun `deleteSession calls dao delete`() = runTest {
        // Given
        coEvery { sessionDao.deleteSession(testSession) } returns Unit

        // When
        repository.deleteSession(testSession)

        // Then
        coVerify { sessionDao.deleteSession(testSession) }
    }

    @Test
    fun `deleteSessionById calls dao delete by id`() = runTest {
        // Given
        coEvery { sessionDao.deleteSessionById(testSessionId) } returns Unit

        // When
        repository.deleteSessionById(testSessionId)

        // Then
        coVerify { sessionDao.deleteSessionById(testSessionId) }
    }

    @Test
    fun `getEventsBySessionId returns flow from dao`() = runTest {
        // Given
        val event = SessionEvent(
            id = "event-1",
            sessionId = testSessionId,
            eventType = SessionEventType.REMINDER_SHOWN,
            timestamp = Date()
        )
        every { sessionEventDao.getEventsBySessionId(testSessionId) } returns flowOf(listOf(event))

        // When
        val result = repository.getEventsBySessionId(testSessionId)

        // Then
        result.collect { events ->
            assertEquals(1, events.size)
            assertEquals(event, events[0])
        }
    }

    @Test
    fun `insertEvent calls dao insert`() = runTest {
        // Given
        val event = SessionEvent(
            id = "event-1",
            sessionId = testSessionId,
            eventType = SessionEventType.ACTION_LOCK,
            timestamp = Date()
        )
        coEvery { sessionEventDao.insertEvent(event) } returns Unit

        // When
        repository.insertEvent(event)

        // Then
        coVerify { sessionEventDao.insertEvent(event) }
    }

    @Test
    fun `getDailyStats calculates correctly for completed sessions`() = runTest {
        // Given
        val completedSession1 = testSession.copy(
            id = "session-1",
            actualDurationMs = 2 * 60 * 60 * 1000L,
            totalReminders = 5,
            totalUnlocks = 3,
            totalSnoozes = 1,
            status = SessionStatus.COMPLETED
        )
        val completedSession2 = testSession.copy(
            id = "session-2",
            actualDurationMs = 3 * 60 * 60 * 1000L,
            totalReminders = 7,
            totalUnlocks = 2,
            totalSnoozes = 2,
            status = SessionStatus.COMPLETED
        )
        
        every { 
            sessionDao.getSessionsByDateRange(any(), any()) 
        } returns flowOf(listOf(completedSession1, completedSession2))

        // When
        val stats = repository.getDailyStats(Date())

        // Then
        assertEquals(5 * 60 * 60 * 1000L, stats.totalDurationMs) // 5 hours
        assertEquals(2, stats.totalSessions)
        assertEquals(12, stats.totalReminders)
        assertEquals(5, stats.totalUnlocks)
        assertEquals(3, stats.totalSnoozes)
        assertEquals(300, stats.totalDurationMinutes) // 5 hours = 300 minutes
        assertEquals(5.0f, stats.totalDurationHours, 0.01f)
        assertEquals(150, stats.averageSessionMinutes) // 300 / 2
    }

    @Test
    fun `getDailyStats excludes non-completed sessions`() = runTest {
        // Given
        val completedSession = testSession.copy(
            actualDurationMs = 2 * 60 * 60 * 1000L,
            status = SessionStatus.COMPLETED
        )
        val runningSession = testSession.copy(
            id = "running-session",
            actualDurationMs = 1 * 60 * 60 * 1000L,
            status = SessionStatus.RUNNING
        )
        val cancelledSession = testSession.copy(
            id = "cancelled-session",
            actualDurationMs = 1 * 60 * 60 * 1000L,
            status = SessionStatus.CANCELLED
        )
        
        every { 
            sessionDao.getSessionsByDateRange(any(), any()) 
        } returns flowOf(listOf(completedSession, runningSession, cancelledSession))

        // When
        val stats = repository.getDailyStats(Date())

        // Then
        assertEquals(2 * 60 * 60 * 1000L, stats.totalDurationMs)
        assertEquals(1, stats.totalSessions)
    }

    @Test
    fun `getDailyStats returns empty stats for no sessions`() = runTest {
        // Given
        every { 
            sessionDao.getSessionsByDateRange(any(), any()) 
        } returns flowOf(emptyList())

        // When
        val stats = repository.getDailyStats(Date())

        // Then
        assertEquals(0L, stats.totalDurationMs)
        assertEquals(0, stats.totalSessions)
        assertEquals(0, stats.totalReminders)
        assertEquals(0, stats.totalUnlocks)
        assertEquals(0, stats.totalSnoozes)
        assertEquals(0, stats.averageSessionMinutes)
    }

    @Test
    fun `getWeeklyStats calculates correctly`() = runTest {
        // Given
        val sessions = listOf(
            testSession.copy(id = "1", actualDurationMs = 4 * 60 * 60 * 1000L),
            testSession.copy(id = "2", actualDurationMs = 3 * 60 * 60 * 1000L)
        )
        every { 
            sessionDao.getSessionsByDateRange(any(), any()) 
        } returns flowOf(sessions)

        // When
        val stats = repository.getWeeklyStats()

        // Then
        assertEquals(7 * 60 * 60 * 1000L, stats.totalDurationMs)
        assertEquals(2, stats.totalSessions)
    }

    @Test
    fun `getMonthlyStats calculates correctly`() = runTest {
        // Given
        val sessions = List(10) { index ->
            testSession.copy(
                id = "session-$index",
                actualDurationMs = 2 * 60 * 60 * 1000L
            )
        }
        every { 
            sessionDao.getSessionsByDateRange(any(), any()) 
        } returns flowOf(sessions)

        // When
        val stats = repository.getMonthlyStats(Date())

        // Then
        assertEquals(20 * 60 * 60 * 1000L, stats.totalDurationMs) // 20 hours
        assertEquals(10, stats.totalSessions)
    }

    @Test
    fun `SessionStats averageSessionMinutes returns 0 when no sessions`() {
        // Given
        val stats = SessionRepository.SessionStats(
            totalDurationMs = 0,
            totalSessions = 0
        )

        // When & Then
        assertEquals(0, stats.averageSessionMinutes)
    }

    @Test
    fun `SessionStats converts duration correctly`() {
        // Given
        val durationMs = 7 * 60 * 60 * 1000L + 30 * 60 * 1000L // 7.5 hours
        val stats = SessionRepository.SessionStats(
            totalDurationMs = durationMs,
            totalSessions = 3
        )

        // When & Then
        assertEquals(450, stats.totalDurationMinutes) // 7.5 * 60
        assertEquals(7.5f, stats.totalDurationHours, 0.01f)
        assertEquals(150, stats.averageSessionMinutes) // 450 / 3
    }
}