package com.nudgr.ui.session_summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nudgr.data.local.entity.Session
import com.nudgr.data.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SessionSummaryUiState {
    data object Loading : SessionSummaryUiState
    data class Success(val session: Session) : SessionSummaryUiState
    data class Error(val message: String) : SessionSummaryUiState
}

@HiltViewModel
class SessionSummaryViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SessionSummaryUiState>(SessionSummaryUiState.Loading)
    val uiState: StateFlow<SessionSummaryUiState> = _uiState.asStateFlow()

    fun loadSession(sessionId: String) {
        viewModelScope.launch {
            _uiState.value = SessionSummaryUiState.Loading
            try {
                val session = sessionRepository.getSessionById(sessionId)
                if (session != null) {
                    _uiState.value = SessionSummaryUiState.Success(session)
                } else {
                    _uiState.value = SessionSummaryUiState.Error("Session not found")
                }
            } catch (e: Exception) {
                _uiState.value = SessionSummaryUiState.Error(
                    e.localizedMessage ?: "Failed to load session data"
                )
            }
        }
    }
}
