package com.nudgr.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nudgr.data.local.entity.Session
import com.nudgr.data.local.entity.SessionStatus
import com.nudgr.data.repository.ImageRepository
import com.nudgr.data.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
    private val sessionRepository: SessionRepository
    // TODO: Add preferences repository for timer settings
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()
    
    fun loadDashboardData() {
        viewModelScope.launch {
            // Load image count
            val imageCount = imageRepository.getActiveImageCount()
            
            // Load current session
            val currentSession = sessionRepository.getCurrentSession()
            
            // Load total sessions count
            val allSessions = sessionRepository.getAllSessions()
            allSessions.collect { sessions ->
                val totalSessions = sessions.size
                val lastSession = sessions.firstOrNull()
                
                _uiState.value = _uiState.value.copy(
                    imageCount = imageCount,
                    totalSessions = totalSessions,
                    lastSession = lastSession,
                    sessionStatus = currentSession?.status ?: SessionStatus.OFF,
                    canStartSession = imageCount >= 1 && currentSession == null // TODO: Add timer validation
                )
            }
        }
    }
    
    fun startSession() {
        viewModelScope.launch {
            // TODO: Implement session start logic
            // 1. Create new session record
            // 2. Start foreground service
            // 3. Seed shuffle bag
            // 4. Track event
            
            // Placeholder
            _uiState.value = _uiState.value.copy(sessionStatus = SessionStatus.RUNNING)
        }
    }
    
    fun endSession() {
        viewModelScope.launch {
            // TODO: Implement session end logic
            // 1. Stop foreground service
            // 2. Update session record
            // 3. Navigate to summary
            
            _uiState.value = _uiState.value.copy(sessionStatus = SessionStatus.OFF)
        }
    }
    
    fun togglePause() {
        viewModelScope.launch {
            val currentStatus = _uiState.value.sessionStatus
            val newStatus = when (currentStatus) {
                SessionStatus.RUNNING -> SessionStatus.PAUSED
                SessionStatus.PAUSED -> SessionStatus.RUNNING
                else -> currentStatus
            }
            
            _uiState.value = _uiState.value.copy(sessionStatus = newStatus)
            
            // TODO: Update session in database and track event
        }
    }
}

data class DashboardUiState(
    val imageCount: Int = 0,
    val totalSessions: Int = 0,
    val lastSession: Session? = null,
    val sessionStatus: SessionStatus = SessionStatus.OFF,
    val canStartSession: Boolean = false,
    val plannedDurationText: String = "4h 0m", // TODO: Load from preferences
    val reminderIntervalText: String = "2m 0s" // TODO: Load from preferences
)
