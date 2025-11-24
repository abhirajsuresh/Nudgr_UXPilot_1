package com.nudgr.ui.images

import android.content.Context
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nudgr.analytics.AnalyticsHelper
import com.nudgr.data.local.entity.Image
import com.nudgr.data.repository.ImageRepository
import io.mockk.coEvery
import io.mockk.coVerify
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
class ImageLibraryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var analyticsHelper: AnalyticsHelper
    private lateinit var imageRepository: ImageRepository
    private lateinit var context: Context
    private lateinit var viewModel: ImageLibraryViewModel

    private val testImage = Image(
        id = "test-image-1",
        name = "test.jpg",
        filePath = "/data/images/test.jpg",
        fileSize = 1024L,
        mimeType = "image/jpeg",
        contentHash = "abc123",
        addedAt = Date(),
        isActive = true
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        analyticsHelper = mockk(relaxed = true)
        imageRepository = mockk()
        context = mockk(relaxed = true)
        
        viewModel = ImageLibraryViewModel(analyticsHelper, imageRepository, context)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is empty`() {
        // When
        val state = viewModel.uiState.value

        // Then
        assertTrue(state.images.isEmpty())
        assertFalse(state.isLoading)
        assertTrue(state.selectedImageIds.isEmpty())
    }

    @Test
    fun `loadImages updates state with images from repository`() = runTest {
        // Given
        val images = listOf(testImage, testImage.copy(id = "test-image-2"))
        every { imageRepository.getAllActiveImages() } returns flowOf(images)

        // When
        viewModel.loadImages()

        // Then
        val state = viewModel.uiState.value
        assertEquals(2, state.images.size)
        assertEquals(images, state.images)
    }

    @Test
    fun `loadImages handles empty list`() = runTest {
        // Given
        every { imageRepository.getAllActiveImages() } returns flowOf(emptyList())

        // When
        viewModel.loadImages()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state.images.isEmpty())
    }

    @Test
    fun `deleteImage removes image and tracks analytics`() = runTest {
        // Given
        coEvery { imageRepository.deleteImage(testImage) } returns Unit

        // When
        viewModel.deleteImage(testImage)

        // Then
        coVerify { imageRepository.deleteImage(testImage) }
        verify { 
            analyticsHelper.trackEvent(
                AnalyticsHelper.Event.IMAGES_DELETED,
                any()
            )
        }
    }

    @Test
    fun `toggleImageSelection adds image to selection`() {
        // Given
        val imageId = "image-1"

        // When
        viewModel.toggleImageSelection(imageId)

        // Then
        val state = viewModel.uiState.value
        assertTrue(state.selectedImageIds.contains(imageId))
    }

    @Test
    fun `toggleImageSelection removes image from selection`() {
        // Given
        val imageId = "image-1"
        viewModel.toggleImageSelection(imageId) // Add first

        // When
        viewModel.toggleImageSelection(imageId) // Remove

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.selectedImageIds.contains(imageId))
    }

    @Test
    fun `selectAllImages selects all images`() = runTest {
        // Given
        val images = listOf(
            testImage.copy(id = "1"),
            testImage.copy(id = "2"),
            testImage.copy(id = "3")
        )
        every { imageRepository.getAllActiveImages() } returns flowOf(images)
        viewModel.loadImages()

        // When
        viewModel.selectAllImages()

        // Then
        val state = viewModel.uiState.value
        assertEquals(3, state.selectedImageIds.size)
        assertTrue(state.selectedImageIds.contains("1"))
        assertTrue(state.selectedImageIds.contains("2"))
        assertTrue(state.selectedImageIds.contains("3"))
    }

    @Test
    fun `clearSelection clears all selections`() {
        // Given
        viewModel.toggleImageSelection("image-1")
        viewModel.toggleImageSelection("image-2")

        // When
        viewModel.clearSelection()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state.selectedImageIds.isEmpty())
    }

    @Test
    fun `deleteSelectedImages deletes multiple images`() = runTest {
        // Given
        val images = listOf(
            testImage.copy(id = "1"),
            testImage.copy(id = "2"),
            testImage.copy(id = "3")
        )
        every { imageRepository.getAllActiveImages() } returns flowOf(images)
        viewModel.loadImages()
        viewModel.toggleImageSelection("1")
        viewModel.toggleImageSelection("2")
        coEvery { imageRepository.deleteImage(any()) } returns Unit

        // When
        viewModel.deleteSelectedImages()

        // Then
        coVerify(exactly = 2) { imageRepository.deleteImage(any()) }
        verify { 
            analyticsHelper.trackEvent(
                AnalyticsHelper.Event.IMAGES_DELETED,
                any()
            )
        }
    }

    @Test
    fun `deleteSelectedImages clears selection after deletion`() = runTest {
        // Given
        val images = listOf(testImage.copy(id = "1"))
        every { imageRepository.getAllActiveImages() } returns flowOf(images)
        viewModel.loadImages()
        viewModel.toggleImageSelection("1")
        coEvery { imageRepository.deleteImage(any()) } returns Unit

        // When
        viewModel.deleteSelectedImages()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state.selectedImageIds.isEmpty())
    }

    @Test
    fun `deleteSelectedImages handles empty selection`() = runTest {
        // When
        viewModel.deleteSelectedImages()

        // Then
        coVerify(exactly = 0) { imageRepository.deleteImage(any()) }
    }

    @Test
    fun `multiple toggles on same image cycles selection correctly`() {
        // Given
        val imageId = "test-image"

        // When & Then
        viewModel.toggleImageSelection(imageId)
        assertTrue(viewModel.uiState.value.selectedImageIds.contains(imageId))

        viewModel.toggleImageSelection(imageId)
        assertFalse(viewModel.uiState.value.selectedImageIds.contains(imageId))

        viewModel.toggleImageSelection(imageId)
        assertTrue(viewModel.uiState.value.selectedImageIds.contains(imageId))
    }
}