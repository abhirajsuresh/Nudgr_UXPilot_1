package com.nudgr.ui.checklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nudgr.data.repository.ImageRepository
import com.nudgr.data.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChecklistViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
    private val sessionRepository: SessionRepository
    // TODO: Add preferences repository for timer and permission states
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ChecklistUiState())
    val uiState: StateFlow<ChecklistUiState> = _uiState.asStateFlow()
    
    fun checkAllRequirements() {
        viewModelScope.launch {
            // Check images
            val imageCount = imageRepository.getActiveImageCount()
            val hasAtLeastOneImage = imageCount >= 1
            
            // Check timers (placeholder - will be implemented with preferences)
            val hasValidTimers = true // TODO: Check from preferences
            
            // Check permissions (placeholder - will be implemented with permission checker)
            val hasCorePermissions = true // TODO: Check actual permissions
            
            _uiState.value = _uiState.value.copy(
                hasAtLeastOneImage = hasAtLeastOneImage,
                hasValidTimers = hasValidTimers,
                hasCorePermissions = hasCorePermissions,
                canContinue = hasAtLeastOneImage && hasValidTimers && hasCorePermissions
            )
        }
    }
}

data class ChecklistUiState(
    val hasAtLeastOneImage: Boolean = false,
    val hasValidTimers: Boolean = false,
    val hasCorePermissions: Boolean = false,
    val canContinue: Boolean = false
)
