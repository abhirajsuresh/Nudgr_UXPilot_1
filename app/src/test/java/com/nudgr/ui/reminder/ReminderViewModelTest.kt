package com.nudgr.ui.reminder

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nudgr.data.local.entity.Image
import com.nudgr.data.repository.ImageRepository
import com.nudgr.service.SessionController
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
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class ReminderViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var imageRepository: ImageRepository
    private lateinit var sessionController: SessionController
    private lateinit var context: Context
    private lateinit var devicePolicyManager: DevicePolicyManager
    private lateinit var viewModel: ReminderViewModel

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
        imageRepository = mockk()
        sessionController = mockk(relaxed = true)
        context = mockk(relaxed = true)
        devicePolicyManager = mockk(relaxed = true)
        
        every { 
            context.getSystemService(Context.DEVICE_POLICY_SERVICE) 
        } returns devicePolicyManager
        
        viewModel = ReminderViewModel(imageRepository, sessionController, context)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has no image`() {
        // When
        val state = viewModel.uiState.value

        // Then
        assertNull(state.imagePath)
        assertNull(state.imageName)
        assertFalse(state.deviceAdminEnabled)
    }

    @Test
    fun `loadRandomImage updates state with image`() = runTest {
        // Given
        coEvery { imageRepository.getRandomActiveImage() } returns testImage
        every { devicePolicyManager.isAdminActive(any<ComponentName>()) } returns false

        // When
        viewModel.loadRandomImage()

        // Then
        val state = viewModel.uiState.value
        assertEquals(testImage.filePath, state.imagePath)
        assertEquals(testImage.name, state.imageName)
    }

    @Test
    fun `loadRandomImage handles null image`() = runTest {
        // Given
        coEvery { imageRepository.getRandomActiveImage() } returns null
        every { devicePolicyManager.isAdminActive(any<ComponentName>()) } returns false

        // When
        viewModel.loadRandomImage()

        // Then
        val state = viewModel.uiState.value
        assertNull(state.imagePath)
        assertNull(state.imageName)
    }

    @Test
    fun `loadRandomImage checks device admin status`() = runTest {
        // Given
        coEvery { imageRepository.getRandomActiveImage() } returns testImage
        every { devicePolicyManager.isAdminActive(any<ComponentName>()) } returns true

        // When
        viewModel.loadRandomImage()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state.deviceAdminEnabled)
    }

    @Test
    fun `loadRandomImage sets device admin disabled when not active`() = runTest {
        // Given
        coEvery { imageRepository.getRandomActiveImage() } returns testImage
        every { devicePolicyManager.isAdminActive(any<ComponentName>()) } returns false

        // When
        viewModel.loadRandomImage()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.deviceAdminEnabled)
    }

    @Test
    fun `lockPhone calls device policy manager`() = runTest {
        // Given
        every { devicePolicyManager.lockNow() } returns Unit

        // When
        viewModel.lockPhone()

        // Then
        verify { devicePolicyManager.lockNow() }
    }

    @Test
    fun `lockPhone handles exception gracefully`() = runTest {
        // Given
        every { devicePolicyManager.lockNow() } throws SecurityException("Not authorized")

        // When & Then - should not crash
        viewModel.lockPhone()
    }

    @Test
    fun `snooze calls session controller with default delay`() = runTest {
        // When
        viewModel.snooze()

        // Then
        verify { sessionController.snoozeNudge(5 * 60 * 1000L) }
    }

    @Test
    fun `extend calls session controller with default extension`() = runTest {
        // When
        viewModel.extend()

        // Then
        verify { sessionController.extendSession(5 * 60 * 1000L) }
    }

    @Test
    fun `snooze handles exception gracefully`() = runTest {
        // Given
        every { sessionController.snoozeNudge(any()) } throws Exception("Service error")

        // When & Then - should not crash
        viewModel.snooze()
    }

    @Test
    fun `extend handles exception gracefully`() = runTest {
        // Given
        every { sessionController.extendSession(any()) } throws Exception("Service error")

        // When & Then - should not crash
        viewModel.extend()
    }

    @Test
    fun `loadRandomImage handles repository exception gracefully`() = runTest {
        // Given
        coEvery { imageRepository.getRandomActiveImage() } throws Exception("Database error")

        // When & Then - should not crash
        viewModel.loadRandomImage()
        
        val state = viewModel.uiState.value
        assertNull(state.imagePath)
    }

    @Test
    fun `multiple loadRandomImage calls can load different images`() = runTest {
        // Given
        val image1 = testImage.copy(id = "image-1", name = "first.jpg")
        val image2 = testImage.copy(id = "image-2", name = "second.jpg")
        coEvery { imageRepository.getRandomActiveImage() } returnsMany listOf(image1, image2)
        every { devicePolicyManager.isAdminActive(any<ComponentName>()) } returns false

        // When
        viewModel.loadRandomImage()
        val state1 = viewModel.uiState.value

        viewModel.loadRandomImage()
        val state2 = viewModel.uiState.value

        // Then
        assertEquals("first.jpg", state1.imageName)
        assertEquals("second.jpg", state2.imageName)
    }
}