package com.nudgr.ui.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nudgr.analytics.AnalyticsHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val analyticsHelper: AnalyticsHelper
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
        analyticsHelper.trackEvent(AnalyticsHelper.Event.ONBOARDING_GUEST_CONTINUE)
        // Future logic for guest session
    }
}

data class WelcomeUiState(
    val isLoading: Boolean = false
)
