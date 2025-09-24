package com.nudgr.ui.permissions

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PermissionsHubViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(PermissionsHubUiState())
    val uiState: StateFlow<PermissionsHubUiState> = _uiState.asStateFlow()
    
    fun checkAllPermissions() {
        viewModelScope.launch {
            val notificationsGranted = checkNotificationPermission()
            val batteryIgnoreGranted = checkBatteryOptimizationPermission()
            val overlayGranted = checkOverlayPermission()
            val storageGranted = checkStoragePermission()
            val deviceAdminGranted = checkDeviceAdminPermission()
            
            val hasCorePermissions = notificationsGranted && batteryIgnoreGranted && overlayGranted
            
            _uiState.value = _uiState.value.copy(
                notificationsGranted = notificationsGranted,
                batteryIgnoreGranted = batteryIgnoreGranted,
                overlayGranted = overlayGranted,
                storageGranted = storageGranted,
                deviceAdminGranted = deviceAdminGranted,
                hasCorePermissions = hasCorePermissions
            )
        }
    }
    
    fun updateNotificationPermission(isGranted: Boolean) {
        _uiState.value = _uiState.value.copy(notificationsGranted = isGranted)
        checkAllPermissions()
    }
    
    fun refreshOverlayPermission() {
        _uiState.value = _uiState.value.copy(overlayGranted = checkOverlayPermission())
        checkAllPermissions()
    }
    
    fun refreshDeviceAdminPermission() {
        _uiState.value = _uiState.value.copy(deviceAdminGranted = checkDeviceAdminPermission())
        checkAllPermissions()
    }
    
    private fun checkNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // Notifications are granted by default on older versions
        }
    }
    
    private fun checkBatteryOptimizationPermission(): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return powerManager.isIgnoringBatteryOptimizations(context.packageName)
    }
    
    private fun checkOverlayPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else {
            true
        }
    }
    
    private fun checkStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
    
    private fun checkDeviceAdminPermission(): Boolean {
        val devicePolicyManager = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val adminComponent = ComponentName(context, "com.nudgr.receiver.DeviceAdminReceiver")
        return devicePolicyManager.isAdminActive(adminComponent)
    }
}

data class PermissionsHubUiState(
    val notificationsGranted: Boolean = false,
    val batteryIgnoreGranted: Boolean = false,
    val overlayGranted: Boolean = false,
    val storageGranted: Boolean = false,
    val deviceAdminGranted: Boolean = false,
    val hasCorePermissions: Boolean = false
)
