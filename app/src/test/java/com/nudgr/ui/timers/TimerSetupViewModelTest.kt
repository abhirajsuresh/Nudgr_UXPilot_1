package com.nudgr.ui.timers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nudgr.data.repository.SettingsRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
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

@OptIn(ExperimentalCoroutinesApi::class)
class TimerSetupViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var settingsRepository: SettingsRepository
    private lateinit var viewModel: TimerSetupViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        settingsRepository = mockk(relaxed = true)
        
        // Default values
        every { settingsRepository.durationMs } returns flowOf(4 * 60 * 60 * 1000L) // 4 hours
        every { settingsRepository.intervalMs } returns flowOf(2 * 60 * 1000L) // 2 minutes
        
        viewModel = TimerSetupViewModel(settingsRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state loads from settings repository`() {
        // When
        val state = viewModel.uiState.value

        // Then
        assertEquals(4, state.durationHours)
        assertEquals(0, state.durationMinutes)
        assertEquals(2, state.intervalMinutes)
        assertEquals(0, state.intervalSeconds)
    }

    @Test
    fun `onDurationHoursChanged updates hours and validates`() {
        // When
        viewModel.onDurationHoursChanged("5")

        // Then
        val state = viewModel.uiState.value
        assertEquals(5, state.durationHours)
        assertTrue(state.isValid)
    }

    @Test
    fun `onDurationHoursChanged with invalid input sets to 0`() {
        // When
        viewModel.onDurationHoursChanged("abc")

        // Then
        val state = viewModel.uiState.value
        assertEquals(0, state.durationHours)
    }

    @Test
    fun `updateDurationMinutes coerces to valid range`() {
        // When
        viewModel.updateDurationMinutes(45)

        // Then
        val state = viewModel.uiState.value
        assertEquals(45, state.durationMinutes)
    }

    @Test
    fun `updateDurationMinutes coerces negative to 0`() {
        // When
        viewModel.updateDurationMinutes(-10)

        // Then
        val state = viewModel.uiState.value
        assertEquals(0, state.durationMinutes)
    }

    @Test
    fun `updateDurationMinutes coerces above 59 to 59`() {
        // When
        viewModel.updateDurationMinutes(100)

        // Then
        val state = viewModel.uiState.value
        assertEquals(59, state.durationMinutes)
    }

    @Test
    fun `updateIntervalMinutes coerces to valid range`() {
        // When
        viewModel.updateIntervalMinutes(5)

        // Then
        val state = viewModel.uiState.value
        assertEquals(5, state.intervalMinutes)
    }

    @Test
    fun `updateIntervalSeconds coerces to valid range`() {
        // When
        viewModel.updateIntervalSeconds(30)

        // Then
        val state = viewModel.uiState.value
        assertEquals(30, state.intervalSeconds)
    }

    @Test
    fun `validation fails when duration less than 1 minute`() {
        // Given
        viewModel.onDurationHoursChanged("0")
        viewModel.updateDurationMinutes(0)

        // When
        val state = viewModel.uiState.value

        // Then
        assertFalse(state.isValid)
        assertEquals("Duration must be at least 1 minute", state.validationMessage)
    }

    @Test
    fun `validation fails when interval less than 10 seconds`() {
        // Given
        viewModel.onDurationHoursChanged("1")
        viewModel.updateIntervalMinutes(0)
        viewModel.updateIntervalSeconds(5)

        // When
        val state = viewModel.uiState.value

        // Then
        assertFalse(state.isValid)
        assertEquals("Interval must be at least 10 seconds", state.validationMessage)
    }

    @Test
    fun `validation fails when interval greater than or equal to duration`() {
        // Given
        viewModel.onDurationHoursChanged("0")
        viewModel.updateDurationMinutes(5)
        viewModel.updateIntervalMinutes(5)
        viewModel.updateIntervalSeconds(0)

        // When
        val state = viewModel.uiState.value

        // Then
        assertFalse(state.isValid)
        assertEquals("Interval must be less than duration", state.validationMessage)
    }

    @Test
    fun `validation passes with valid values`() {
        // Given
        viewModel.onDurationHoursChanged("2")
        viewModel.updateDurationMinutes(30)
        viewModel.updateIntervalMinutes(3)
        viewModel.updateIntervalSeconds(0)

        // When
        val state = viewModel.uiState.value

        // Then
        assertTrue(state.isValid)
        assertEquals("", state.validationMessage)
    }

    @Test
    fun `resetToDefaults sets default values`() {
        // Given
        viewModel.onDurationHoursChanged("8")
        viewModel.updateIntervalMinutes(10)

        // When
        viewModel.resetToDefaults()

        // Then
        val state = viewModel.uiState.value
        assertEquals(4, state.durationHours)
        assertEquals(0, state.durationMinutes)
        assertEquals(2, state.intervalMinutes)
        assertEquals(0, state.intervalSeconds)
    }

    @Test
    fun `saveTimers calls repository when valid`() = runTest {
        // Given
        viewModel.onDurationHoursChanged("3")
        viewModel.updateDurationMinutes(0)
        viewModel.updateIntervalMinutes(1)
        viewModel.updateIntervalSeconds(30)

        // When
        viewModel.saveTimers()

        // Then
        coVerify { 
            settingsRepository.saveTimers(
                3 * 60 * 60 * 1000L, // 3 hours
                90 * 1000L // 1 minute 30 seconds
            )
        }
    }

    @Test
    fun `saveTimers does not call repository when invalid`() = runTest {
        // Given
        viewModel.onDurationHoursChanged("0")
        viewModel.updateDurationMinutes(0)

        // When
        viewModel.saveTimers()

        // Then
        coVerify(exactly = 0) { settingsRepository.saveTimers(any(), any()) }
    }

    @Test
    fun `durationMs calculation is correct`() {
        // Given
        viewModel.onDurationHoursChanged("2")
        viewModel.updateDurationMinutes(30)

        // When
        val state = viewModel.uiState.value

        // Then
        val expectedMs = (2 * 60 + 30) * 60 * 1000L // 2.5 hours in ms
        assertEquals(expectedMs, state.durationMs)
    }

    @Test
    fun `intervalMs calculation is correct`() {
        // Given
        viewModel.updateIntervalMinutes(3)
        viewModel.updateIntervalSeconds(45)

        // When
        val state = viewModel.uiState.value

        // Then
        val expectedMs = (3 * 60 + 45) * 1000L // 3:45 in ms
        assertEquals(expectedMs, state.intervalMs)
    }

    @Test
    fun `minimum valid interval is 10 seconds`() {
        // Given
        viewModel.onDurationHoursChanged("1")
        viewModel.updateIntervalMinutes(0)
        viewModel.updateIntervalSeconds(10)

        // When
        val state = viewModel.uiState.value

        // Then
        assertTrue(state.isValid)
        assertEquals(10000L, state.intervalMs)
    }

    @Test
    fun `edge case - interval just below duration is valid`() {
        // Given
        viewModel.onDurationHoursChanged("0")
        viewModel.updateDurationMinutes(5)
        viewModel.updateIntervalMinutes(4)
        viewModel.updateIntervalSeconds(59)

        // When
        val state = viewModel.uiState.value

        // Then
        assertTrue(state.isValid)
    }
}