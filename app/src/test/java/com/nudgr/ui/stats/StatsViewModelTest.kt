package com.nudgr.ui.stats

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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

@OptIn(ExperimentalCoroutinesApi::class)
class StatsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var sessionRepository: SessionRepository
    private lateinit var viewModel: StatsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        sessionRepository = mockk()
        viewModel = StatsViewModel(sessionRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has empty stats`() {
        // When
        val todayStats = viewModel.todayStats.value
        val weeklyStats = viewModel.weeklyStats.value
        val monthlyStats = viewModel.monthlyStats.value

        // Then
        assertEquals(0L, todayStats.totalDurationMs)
        assertEquals(0, todayStats.totalSessions)
        assertEquals(0L, weeklyStats.totalDurationMs)
        assertEquals(0, weeklyStats.totalSessions)
        assertEquals(0L, monthlyStats.totalDurationMs)
        assertEquals(0, monthlyStats.totalSessions)
    }

    @Test
    fun `loadStats updates today stats`() = runTest {
        // Given
        val expectedStats = SessionRepository.SessionStats(
            totalDurationMs = 2 * 60 * 60 * 1000L,
            totalSessions = 3,
            totalReminders = 15,
            totalUnlocks = 8,
            totalSnoozes = 2
        )
        coEvery { sessionRepository.getDailyStats() } returns expectedStats
        coEvery { sessionRepository.getWeeklyStats() } returns SessionRepository.SessionStats()
        coEvery { sessionRepository.getMonthlyStats() } returns SessionRepository.SessionStats()

        // When
        viewModel.loadStats()

        // Then
        val stats = viewModel.todayStats.value
        assertEquals(expectedStats.totalDurationMs, stats.totalDurationMs)
        assertEquals(expectedStats.totalSessions, stats.totalSessions)
        assertEquals(expectedStats.totalReminders, stats.totalReminders)
        assertEquals(expectedStats.totalUnlocks, stats.totalUnlocks)
        assertEquals(expectedStats.totalSnoozes, stats.totalSnoozes)
    }

    @Test
    fun `loadStats updates weekly stats`() = runTest {
        // Given
        val expectedStats = SessionRepository.SessionStats(
            totalDurationMs = 10 * 60 * 60 * 1000L,
            totalSessions = 12,
            totalReminders = 60,
            totalUnlocks = 35,
            totalSnoozes = 8
        )
        coEvery { sessionRepository.getDailyStats() } returns SessionRepository.SessionStats()
        coEvery { sessionRepository.getWeeklyStats() } returns expectedStats
        coEvery { sessionRepository.getMonthlyStats() } returns SessionRepository.SessionStats()

        // When
        viewModel.loadStats()

        // Then
        val stats = viewModel.weeklyStats.value
        assertEquals(expectedStats.totalDurationMs, stats.totalDurationMs)
        assertEquals(expectedStats.totalSessions, stats.totalSessions)
        assertEquals(expectedStats.totalReminders, stats.totalReminders)
    }

    @Test
    fun `loadStats updates monthly stats`() = runTest {
        // Given
        val expectedStats = SessionRepository.SessionStats(
            totalDurationMs = 40 * 60 * 60 * 1000L,
            totalSessions = 50,
            totalReminders = 250,
            totalUnlocks = 150,
            totalSnoozes = 30
        )
        coEvery { sessionRepository.getDailyStats() } returns SessionRepository.SessionStats()
        coEvery { sessionRepository.getWeeklyStats() } returns SessionRepository.SessionStats()
        coEvery { sessionRepository.getMonthlyStats() } returns expectedStats

        // When
        viewModel.loadStats()

        // Then
        val stats = viewModel.monthlyStats.value
        assertEquals(expectedStats.totalDurationMs, stats.totalDurationMs)
        assertEquals(expectedStats.totalSessions, stats.totalSessions)
    }

    @Test
    fun `loadStats updates all stats simultaneously`() = runTest {
        // Given
        val todayExpected = SessionRepository.SessionStats(totalDurationMs = 1000L, totalSessions = 1)
        val weeklyExpected = SessionRepository.SessionStats(totalDurationMs = 7000L, totalSessions = 7)
        val monthlyExpected = SessionRepository.SessionStats(totalDurationMs = 30000L, totalSessions = 30)
        
        coEvery { sessionRepository.getDailyStats() } returns todayExpected
        coEvery { sessionRepository.getWeeklyStats() } returns weeklyExpected
        coEvery { sessionRepository.getMonthlyStats() } returns monthlyExpected

        // When
        viewModel.loadStats()

        // Then
        assertEquals(todayExpected.totalDurationMs, viewModel.todayStats.value.totalDurationMs)
        assertEquals(weeklyExpected.totalDurationMs, viewModel.weeklyStats.value.totalDurationMs)
        assertEquals(monthlyExpected.totalDurationMs, viewModel.monthlyStats.value.totalDurationMs)
    }

    @Test
    fun `loadStats handles exception gracefully`() = runTest {
        // Given
        coEvery { sessionRepository.getDailyStats() } throws Exception("Database error")
        coEvery { sessionRepository.getWeeklyStats() } throws Exception("Database error")
        coEvery { sessionRepository.getMonthlyStats() } throws Exception("Database error")

        // When
        viewModel.loadStats()

        // Then - should not crash, stats remain at default values
        assertEquals(0L, viewModel.todayStats.value.totalDurationMs)
        assertEquals(0L, viewModel.weeklyStats.value.totalDurationMs)
        assertEquals(0L, viewModel.monthlyStats.value.totalDurationMs)
    }

    @Test
    fun `loadStats with zero sessions returns empty stats`() = runTest {
        // Given
        val emptyStats = SessionRepository.SessionStats()
        coEvery { sessionRepository.getDailyStats() } returns emptyStats
        coEvery { sessionRepository.getWeeklyStats() } returns emptyStats
        coEvery { sessionRepository.getMonthlyStats() } returns emptyStats

        // When
        viewModel.loadStats()

        // Then
        assertEquals(0L, viewModel.todayStats.value.totalDurationMs)
        assertEquals(0, viewModel.todayStats.value.totalSessions)
        assertEquals(0, viewModel.todayStats.value.averageSessionMinutes)
    }

    @Test
    fun `loadStats calculates derived properties correctly`() = runTest {
        // Given
        val stats = SessionRepository.SessionStats(
            totalDurationMs = 3 * 60 * 60 * 1000L, // 3 hours
            totalSessions = 2,
            totalReminders = 10,
            totalUnlocks = 5,
            totalSnoozes = 1
        )
        coEvery { sessionRepository.getDailyStats() } returns stats
        coEvery { sessionRepository.getWeeklyStats() } returns SessionRepository.SessionStats()
        coEvery { sessionRepository.getMonthlyStats() } returns SessionRepository.SessionStats()

        // When
        viewModel.loadStats()

        // Then
        val todayStats = viewModel.todayStats.value
        assertEquals(180, todayStats.totalDurationMinutes) // 3 hours = 180 minutes
        assertEquals(3.0f, todayStats.totalDurationHours, 0.01f)
        assertEquals(90, todayStats.averageSessionMinutes) // 180 / 2
    }
}