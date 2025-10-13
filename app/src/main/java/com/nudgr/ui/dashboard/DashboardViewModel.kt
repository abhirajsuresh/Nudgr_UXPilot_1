package com.nudgr.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nudgr.analytics.AnalyticsHelper
import com.nudgr.data.local.entity.Session
import com.nudgr.data.local.entity.SessionStatus
import com.nudgr.data.repository.ImageRepository
import com.nudgr.data.repository.SessionRepository
import com.nudgr.data.repository.SettingsRepository
import com.nudgr.service.SessionController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val analyticsHelper: AnalyticsHelper,
    private val imageRepository: ImageRepository,
    private val sessionRepository: SessionRepository,
    private val settingsRepository: SettingsRepository,
    private val sessionController: SessionController
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
                val lastSession = sessions.firstOrNull { it.status == SessionStatus.COMPLETED }
                
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
            val durationMs = settingsRepository.durationMs.first()
            val intervalMs = settingsRepository.intervalMs.first()
            sessionController.startSession(durationMs, intervalMs)
            analyticsHelper.trackEvent(AnalyticsHelper.Event.SESSION_START)
            loadDashboardData() // Refresh state
        }
    }
    
    fun endSession() {
        viewModelScope.launch {
            sessionController.endSession()
            analyticsHelper.trackEvent(AnalyticsHelper.Event.SESSION_END_MANUAL)
            loadDashboardData() // Refresh state
        }
    }
    
    fun togglePause() {
        viewModelScope.launch {
            val currentStatus = _uiState.value.sessionStatus
            if (currentStatus == SessionStatus.RUNNING) {
                sessionController.pauseSession()
                analyticsHelper.trackEvent(AnalyticsHelper.Event.SESSION_PAUSE)
            } else if (currentStatus == SessionStatus.PAUSED) {
                sessionController.resumeSession()
                analyticsHelper.trackEvent(AnalyticsHelper.Event.SESSION_RESUME)
            }
            loadDashboardData() // Refresh state
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
