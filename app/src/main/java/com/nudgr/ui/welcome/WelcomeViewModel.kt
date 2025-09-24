package com.nudgr.ui.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    // TODO: Add analytics service when ready
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(WelcomeUiState())
    val uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()
    
    fun trackAppOpen() {
        viewModelScope.launch {
            // TODO: Track nudgr_app_open event
            // analytics.track("nudgr_app_open")
        }
    }
    
    fun onGuestContinue() {
        viewModelScope.launch {
            // TODO: Track nudgr_onboarding_step_complete with step:'auth'
            // analytics.track("nudgr_onboarding_step_complete", mapOf("step" to "auth"))
        }
    }
}

data class WelcomeUiState(
    val isLoading: Boolean = false
)
