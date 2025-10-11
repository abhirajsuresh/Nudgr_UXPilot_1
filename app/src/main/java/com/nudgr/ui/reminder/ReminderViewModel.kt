package com.nudgr.ui.reminder

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nudgr.data.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
    private val sessionController: com.nudgr.service.SessionController,
    @ApplicationContext private val context: Context
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ReminderUiState())
    val uiState: StateFlow<ReminderUiState> = _uiState.asStateFlow()
    
    fun loadRandomImage() {
        viewModelScope.launch {
            try {
                val randomImage = imageRepository.getRandomActiveImage()
                _uiState.value = _uiState.value.copy(
                    imagePath = randomImage?.filePath,
                    imageName = randomImage?.name
                )
                
                // Check device admin status
                val devicePolicyManager = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
                val adminComponent = ComponentName(context, "com.nudgr.receiver.DeviceAdminReceiver")
                val deviceAdminEnabled = devicePolicyManager.isAdminActive(adminComponent)
                
                _uiState.value = _uiState.value.copy(deviceAdminEnabled = deviceAdminEnabled)
                
                // TODO: Track nudgr_reminder_shown event
                // analytics.track("nudgr_reminder_shown")
                
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun lockPhone() {
        viewModelScope.launch {
            try {
                val devicePolicyManager = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
                devicePolicyManager.lockNow()
                
                // TODO: Track nudgr_action_lock event
                // analytics.track("nudgr_action_lock")
                
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun snooze() {
        viewModelScope.launch {
            try {
                // Snooze for 5 minutes (default)
                sessionController.snoozeNudge(5 * 60 * 1000L)
                // TODO: Track nudgr_action_snooze event
                // analytics.track("nudgr_action_snooze")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun extend() {
        viewModelScope.launch {
            try {
                // Extend session by 5 minutes (default)
                sessionController.extendSession(5 * 60 * 1000L)
                // TODO: Track nudgr_action_extend event
                // analytics.track("nudgr_action_extend")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

data class ReminderUiState(
    val imagePath: String? = null,
    val imageName: String? = null,
    val deviceAdminEnabled: Boolean = false
)
