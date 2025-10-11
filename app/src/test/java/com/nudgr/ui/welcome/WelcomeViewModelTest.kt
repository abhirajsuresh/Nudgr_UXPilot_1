package com.nudgr.ui.welcome

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nudgr.analytics.AnalyticsHelper
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WelcomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var analyticsHelper: AnalyticsHelper
    private lateinit var viewModel: WelcomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        analyticsHelper = mockk(relaxed = true)
        viewModel = WelcomeViewModel(analyticsHelper)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is not loading`() {
        // When
        val state = viewModel.uiState.value

        // Then
        assertFalse(state.isLoading)
    }

    @Test
    fun `onGuestContinue tracks analytics event`() {
        // When
        viewModel.onGuestContinue()

        // Then
        verify { 
            analyticsHelper.trackEvent(AnalyticsHelper.Event.ONBOARDING_GUEST_CONTINUE)
        }
    }

    @Test
    fun `trackAppOpen can be called without errors`() {
        // When & Then - should not crash
        viewModel.trackAppOpen()
    }

    @Test
    fun `multiple onGuestContinue calls track multiple events`() {
        // When
        viewModel.onGuestContinue()
        viewModel.onGuestContinue()
        viewModel.onGuestContinue()

        // Then
        verify(exactly = 3) { 
            analyticsHelper.trackEvent(AnalyticsHelper.Event.ONBOARDING_GUEST_CONTINUE)
        }
    }
}