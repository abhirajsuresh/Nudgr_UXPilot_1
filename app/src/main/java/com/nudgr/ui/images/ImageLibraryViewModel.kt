package com.nudgr.ui.images

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.os.Bundle
import com.nudgr.analytics.AnalyticsHelperimport com.nudgr.data.local.entity.Image
import com.nudgr.data.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.security.MessageDigest
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ImageLibraryViewModel @Inject constructor(
    private val analyticsHelper: AnalyticsHelper,
    private val imageRepository: ImageRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ImageLibraryUiState())
    val uiState: StateFlow<ImageLibraryUiState> = _uiState.asStateFlow()
    
    fun loadImages() {
        viewModelScope.launch {
            imageRepository.getAllActiveImages().collect { images ->
                _uiState.value = _uiState.value.copy(images = images)
            }
        }
    }
    
    fun importImages(uris: List<Uri>) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val importedImages = mutableListOf<Image>()
                
                for (uri in uris) {
                    try {
                        val image = importSingleImage(uri)
                        if (image != null) {
                            // Check for duplicates
                            val existingImage = imageRepository.getImageByHash(image.contentHash)
                            if (existingImage == null) {
                                imageRepository.insertImage(image)
                                importedImages.add(image)
                            }
                        }
                    } catch (e: Exception) {
                        // Log error but continue with other images
                        e.printStackTrace()
                    }
                }
                
                analyticsHelper.trackEvent(AnalyticsHelper.Event.IMAGES_ADDED, Bundle().apply {
                    putInt(AnalyticsHelper.Param.IMAGE_COUNT, importedImages.size)
                })
                
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
    
    private suspend fun importSingleImage(uri: Uri): Image? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            inputStream?.use { stream ->
                // Create app-specific directory
                val imagesDir = File(context.filesDir, "images")
                if (!imagesDir.exists()) {
                    imagesDir.mkdirs()
                }
                
                // Generate unique filename
                val fileName = "img_${System.currentTimeMillis()}_${UUID.randomUUID().toString().substring(0, 8)}.jpg"
                val file = File(imagesDir, fileName)
                
                // Copy file
                val outputStream = FileOutputStream(file)
                stream.copyTo(outputStream)
                outputStream.close()
                
                // Get file info
                val fileSize = file.length()
                val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
                
                // Generate content hash
                val contentHash = generateContentHash(file)
                
                // Get display name
                val displayName = getDisplayName(uri) ?: fileName
                
                Image(
                    id = UUID.randomUUID().toString(),
                    name = displayName,
                    filePath = file.absolutePath,
                    fileSize = fileSize,
                    mimeType = mimeType,
                    contentHash = contentHash,
                    addedAt = Date(),
                    isActive = true
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    private fun generateContentHash(file: File): String {
        val digest = MessageDigest.getInstance("SHA-256")
        file.inputStream().use { inputStream ->
            val buffer = ByteArray(8192)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                digest.update(buffer, 0, bytesRead)
            }
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }
    
    private fun getDisplayName(uri: Uri): String? {
        return try {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    if (nameIndex >= 0) {
                        it.getString(nameIndex)
                    } else null
                } else null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    fun deleteImage(image: Image) {
        viewModelScope.launch {
            try {
                // Delete file
                val file = File(image.filePath)
                if (file.exists()) {
                    file.delete()
                }
                
                // Delete from database
                imageRepository.deleteImage(image)

                analyticsHelper.trackEvent(AnalyticsHelper.Event.IMAGES_DELETED, Bundle().apply {
                    putInt(AnalyticsHelper.Param.IMAGE_COUNT, 1)
                })
                
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteSelectedImages() {
        viewModelScope.launch {
            val selectedImages = _uiState.value.images.filter { it.id in _uiState.value.selectedImageIds }
            val deletedCount = selectedImages.size
            selectedImages.forEach { image ->
                try {
                    val file = File(image.filePath)
                    if (file.exists()) {
                        file.delete()
                    }
                    imageRepository.deleteImage(image)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            analyticsHelper.trackEvent(AnalyticsHelper.Event.IMAGES_DELETED, Bundle().apply {
                putInt(AnalyticsHelper.Param.IMAGE_COUNT, deletedCount)
            })
            clearSelection()
        }
    }
    fun toggleImageSelection(imageId: String) {
        val currentSelection = _uiState.value.selectedImageIds
        val newSelection = if (imageId in currentSelection) {
            currentSelection - imageId
        } else {
            currentSelection + imageId
        }
        _uiState.value = _uiState.value.copy(selectedImageIds = newSelection)
    }

    fun selectAllImages() {
        val allImageIds = _uiState.value.images.map { it.id }.toSet()
        _uiState.value = _uiState.value.copy(selectedImageIds = allImageIds)
    }

    fun clearSelection() {
        _uiState.value = _uiState.value.copy(selectedImageIds = emptySet())
    }
}

data class ImageLibraryUiState(
    val images: List<Image> = emptyList(),
    val isLoading: Boolean = false,
    val selectedImageIds: Set<String> = emptySet()
)
