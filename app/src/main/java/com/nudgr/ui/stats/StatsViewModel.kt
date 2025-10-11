package com.nudgr.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nudgr.data.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {
    
    private val _todayStats = MutableStateFlow(SessionRepository.SessionStats())
    val todayStats: StateFlow<SessionRepository.SessionStats> = _todayStats.asStateFlow()
    
    private val _weeklyStats = MutableStateFlow(SessionRepository.SessionStats())
    val weeklyStats: StateFlow<SessionRepository.SessionStats> = _weeklyStats.asStateFlow()
    
    private val _monthlyStats = MutableStateFlow(SessionRepository.SessionStats())
    val monthlyStats: StateFlow<SessionRepository.SessionStats> = _monthlyStats.asStateFlow()
    
    fun loadStats() {
        viewModelScope.launch {
            try {
                _todayStats.value = sessionRepository.getDailyStats()
                _weeklyStats.value = sessionRepository.getWeeklyStats()
                _monthlyStats.value = sessionRepository.getMonthlyStats()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

