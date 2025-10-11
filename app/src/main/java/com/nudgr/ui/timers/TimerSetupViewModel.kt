package com.nudgr.ui.timers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nudgr.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerSetupViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(TimerSetupUiState())
    val uiState: StateFlow<TimerSetupUiState> = _uiState.asStateFlow()
    
    init {
        viewModelScope.launch {
            combine(
                settingsRepository.durationMs,
                settingsRepository.intervalMs
            ) { duration, interval ->
                Pair(duration, interval)
            }.collect { (duration, interval) ->
                _uiState.value = _uiState.value.copy(
                    durationHours = (duration / (1000 * 60 * 60)).toInt(),
                    durationMinutes = ((duration / (1000 * 60)) % 60).toInt(),
                    intervalMinutes = (interval / (1000 * 60)).toInt(),
                    intervalSeconds = ((interval / 1000) % 60).toInt()
                )
            }
        }
    }
    
    fun onDurationHoursChanged(hours: String) {
        _uiState.value = _uiState.value.copy(durationHours = hours.toIntOrNull() ?: 0)
        validateSettings()
    }
    
    fun updateDurationMinutes(minutes: Int) {
        _uiState.value = _uiState.value.copy(durationMinutes = minutes.coerceIn(0, 59))
        validateSettings()
    }
    
    fun updateIntervalMinutes(minutes: Int) {
        _uiState.value = _uiState.value.copy(intervalMinutes = minutes.coerceIn(0, 59))
        validateSettings()
    }
    
    fun updateIntervalSeconds(seconds: Int) {
        _uiState.value = _uiState.value.copy(intervalSeconds = seconds.coerceIn(0, 59))
        validateSettings()
    }
    
    private fun validateSettings() {
        val state = _uiState.value
        val durationMs = (state.durationHours * 60 + state.durationMinutes) * 60 * 1000L
        val intervalMs = (state.intervalMinutes * 60 + state.intervalSeconds) * 1000L
        
        val validationMessage = when {
            durationMs < 60000 -> "Duration must be at least 1 minute"
            intervalMs < 10000 -> "Interval must be at least 10 seconds"
            intervalMs >= durationMs -> "Interval must be less than duration"
            else -> ""
        }
        
        val isValid = validationMessage.isEmpty()
        
        _uiState.value = state.copy(
            validationMessage = validationMessage,
            isValid = isValid,
            durationMs = durationMs,
            intervalMs = intervalMs
        )
    }
    
    fun resetToDefaults() {
        _uiState.value = _uiState.value.copy(
            durationHours = 4,
            durationMinutes = 0,
            intervalMinutes = 2,
            intervalSeconds = 0
        )
        validateSettings()
    }
    
    fun saveTimers() {
        if (!_uiState.value.isValid) return

        viewModelScope.launch {
            settingsRepository.saveTimers(
                _uiState.value.durationMs,
                _uiState.value.intervalMs
            )
        }
    }}

data class TimerSetupUiState(
    val durationHours: Int = 0,
    val durationMinutes: Int = 0,
    val intervalMinutes: Int = 0,
    val intervalSeconds: Int = 0,
    val durationMs: Long = 0,
    val intervalMs: Long = 0,
    val validationMessage: String = "",
    val isValid: Boolean = false
)
