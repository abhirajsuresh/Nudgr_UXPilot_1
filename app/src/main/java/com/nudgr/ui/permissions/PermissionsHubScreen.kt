package com.nudgr.ui.permissions

import android.Manifest
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nudgr.core.ui.components.NudgrButton
import com.nudgr.core.ui.components.ButtonVariant
import com.nudgr.core.ui.theme.NudgrColors
import com.nudgr.core.ui.theme.NudgrGradients

@Composable
fun PermissionsHubScreen(
    navController: NavController,
    viewModel: PermissionsHubViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.updateNotificationPermission(isGranted)
    }
    
    val overlayPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.refreshOverlayPermission()
    }
    
    val deviceAdminLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.refreshDeviceAdminPermission()
    }
    
    LaunchedEffect(Unit) {
        viewModel.checkAllPermissions()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(NudgrGradients.Screen))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Text(
                        text = "Back",
                        color = NudgrColors.TextSecondary,
                        fontSize = 16.sp
                    )
                }
                
                Text(
                    text = "Permissions",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = NudgrColors.TextPrimary,
                    letterSpacing = 0.5.sp
                )
                
                TextButton(
                    onClick = { viewModel.checkAllPermissions() }
                ) {
                    Text(
                        text = "Recheck",
                        color = NudgrColors.Primary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Permission items
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PermissionItem(
                    title = "Notifications",
                    description = "Required for reminder notifications",
                    icon = Icons.Rounded.Notifications,
                    isGranted = uiState.notificationsGranted,
                    onRequest = {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                )
                
                PermissionItem(
                    title = "Battery Optimization",
                    description = "Prevent app from being killed in background",
                    icon = Icons.Rounded.BatterySaver,
                    isGranted = uiState.batteryIgnoreGranted,
                    onRequest = {
                        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                            data = Uri.parse("package:${context.packageName}")
                        }
                        overlayPermissionLauncher.launch(intent)
                    }
                )
                
                PermissionItem(
                    title = "Overlay Permission",
                    description = "Show reminders over other apps",
                    icon = Icons.Rounded.Layers,
                    isGranted = uiState.overlayGranted,
                    onRequest = {
                        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                            data = Uri.parse("package:${context.packageName}")
                        }
                        overlayPermissionLauncher.launch(intent)
                    }
                )
                
                PermissionItem(
                    title = "Storage Access",
                    description = "Access images from gallery",
                    icon = Icons.Rounded.Storage,
                    isGranted = uiState.storageGranted,
                    onRequest = {
                        // This is handled by the image picker automatically
                    }
                )
                
                PermissionItem(
                    title = "Device Admin",
                    description = "Lock phone when requested (optional)",
                    icon = Icons.Rounded.Security,
                    isGranted = uiState.deviceAdminGranted,
                    onRequest = {
                        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                            putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, ComponentName(context, "com.nudgr.receiver.DeviceAdminReceiver"))
                            putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Required to lock phone during focus sessions")
                        }
                        deviceAdminLauncher.launch(intent)
                    }
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Continue button
            NudgrButton(
                text = "Continue Setup",
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.hasCorePermissions,
                variant = ButtonVariant.Primary
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PermissionItem(
    title: String,
    description: String,
    icon: ImageVector,
    isGranted: Boolean,
    onRequest: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isGranted) {
                NudgrColors.Success.copy(alpha = 0.1f)
            } else {
                NudgrColors.Gray800.copy(alpha = 0.5f)
            }
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isGranted) 2.dp else 1.dp,
            color = if (isGranted) NudgrColors.Success else NudgrColors.Gray700
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        brush = if (isGranted) {
                            Brush.linearGradient(NudgrGradients.Success)
                        } else {
                            Brush.linearGradient(listOf(NudgrColors.Gray700, NudgrColors.Gray600))
                        },
                        shape = androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            // Text content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isGranted) Color.White else NudgrColors.Gray300
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = if (isGranted) NudgrColors.Gray300 else NudgrColors.Gray400
                )
            }
            
            // Action button
            if (!isGranted) {
                TextButton(
                    onClick = onRequest
                ) {
                    Text(
                        text = "Grant",
                        color = NudgrColors.Primary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Granted",
                    tint = NudgrColors.Success,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
