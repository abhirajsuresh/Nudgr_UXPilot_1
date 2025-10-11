package com.nudgr.ui.checklist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nudgr.analytics.AnalyticsHelper
import com.nudgr.data.repository.ImageRepository
import com.nudgr.data.repository.SessionRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
class ChecklistViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var analyticsHelper: AnalyticsHelper
    private lateinit var imageRepository: ImageRepository
    private lateinit var sessionRepository: SessionRepository
    private lateinit var viewModel: ChecklistViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        analyticsHelper = mockk(relaxed = true)
        imageRepository = mockk()
        sessionRepository = mockk()
        viewModel = ChecklistViewModel(analyticsHelper, imageRepository, sessionRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has all requirements false`() {
        // When
        val state = viewModel.uiState.value

        // Then
        assertFalse(state.hasAtLeastOneImage)
        assertFalse(state.hasValidTimers)
        assertFalse(state.hasCorePermissions)
        assertFalse(state.canContinue)
    }

    @Test
    fun `checkAllRequirements updates hasAtLeastOneImage when images exist`() = runTest {
        // Given
        coEvery { imageRepository.getActiveImageCount() } returns 5

        // When
        viewModel.checkAllRequirements()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state.hasAtLeastOneImage)
    }

    @Test
    fun `checkAllRequirements sets hasAtLeastOneImage false when no images`() = runTest {
        // Given
        coEvery { imageRepository.getActiveImageCount() } returns 0

        // When
        viewModel.checkAllRequirements()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.hasAtLeastOneImage)
    }

    @Test
    fun `checkAllRequirements sets canContinue true when all requirements met`() = runTest {
        // Given
        coEvery { imageRepository.getActiveImageCount() } returns 1

        // When
        viewModel.checkAllRequirements()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state.canContinue)
    }

    @Test
    fun `checkAllRequirements sets canContinue false when requirements not met`() = runTest {
        // Given
        coEvery { imageRepository.getActiveImageCount() } returns 0

        // When
        viewModel.checkAllRequirements()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.canContinue)
    }

    @Test
    fun `onChecklistCompleted tracks analytics event`() {
        // When
        viewModel.onChecklistCompleted()

        // Then
        verify { 
            analyticsHelper.trackEvent(AnalyticsHelper.Event.ONBOARDING_CHECKLIST_COMPLETE) 
        }
    }

    @Test
    fun `checkAllRequirements with exactly one image meets requirement`() = runTest {
        // Given
        coEvery { imageRepository.getActiveImageCount() } returns 1

        // When
        viewModel.checkAllRequirements()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state.hasAtLeastOneImage)
        assertTrue(state.canContinue)
    }

    @Test
    fun `checkAllRequirements with multiple images meets requirement`() = runTest {
        // Given
        coEvery { imageRepository.getActiveImageCount() } returns 100

        // When
        viewModel.checkAllRequirements()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state.hasAtLeastOneImage)
    }
}