package com.nudgr.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.shadow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nudgr.core.ui.components.NudgrButton
import com.nudgr.core.ui.components.ButtonVariant
import com.nudgr.core.ui.theme.NudgrColors
import com.nudgr.core.ui.theme.NudgrGradients
import com.nudgr.data.local.entity.SessionStatus

@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadDashboardData()
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
                Text(
                    text = "Dashboard",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = NudgrColors.TextPrimary,
                    letterSpacing = 0.5.sp
                )
                
                IconButton(
                    onClick = { /* TODO: Open settings */ }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = "Settings",
                        tint = NudgrColors.TextSecondary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Session Card
            SessionCard(
                uiState = uiState,
                onStartSession = { viewModel.startSession() },
                onEndSession = { viewModel.endSession() },
                onPauseResume = { viewModel.togglePause() }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Quick Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    StatCard(
                        title = "Images",
                        value = uiState.imageCount.toString(),
                        icon = Icons.Rounded.Image,
                        onClick = { navController.navigate("image_library") }
                    )
                }
                
                Box(modifier = Modifier.weight(1f)) {
                    StatCard(
                        title = "Sessions",
                        value = uiState.totalSessions.toString(),
                        icon = Icons.Rounded.Timer,
                        onClick = { navController.navigate("reports_overview") }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Quick Actions
            Text(
                text = "Quick Actions",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = NudgrColors.TextPrimary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    ActionButton(
                        text = "Timers",
                        icon = Icons.Rounded.Schedule,
                        onClick = { navController.navigate("timer_setup") }
                    )
                }
                
                Box(modifier = Modifier.weight(1f)) {
                    ActionButton(
                        text = "Permissions",
                        icon = Icons.Rounded.Security,
                        onClick = { navController.navigate("permissions_hub") }
                    )
                }
                
                Box(modifier = Modifier.weight(1f)) {
                    ActionButton(
                        text = "Reports",
                        icon = Icons.Rounded.Assessment,
                        onClick = { navController.navigate("reports_overview") }
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Last Session Summary
            val lastSession = uiState.lastSession
            if (lastSession != null) {
                LastSessionCard(
                    session = lastSession,
                    onViewReport = { navController.navigate("report_detail/${lastSession.id}") }
                )
            }
        }
    }
}

@Composable
private fun SessionCard(
    uiState: DashboardUiState,
    onStartSession: () -> Unit,
    onEndSession: () -> Unit,
    onPauseResume: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (uiState.sessionStatus) {
                SessionStatus.RUNNING -> NudgrColors.Success.copy(alpha = 0.1f)
                SessionStatus.PAUSED -> NudgrColors.Warning.copy(alpha = 0.1f)
                else -> NudgrColors.Gray800.copy(alpha = 0.5f)
            }
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 2.dp,
            color = when (uiState.sessionStatus) {
                SessionStatus.RUNNING -> NudgrColors.Success
                SessionStatus.PAUSED -> NudgrColors.Warning
                else -> NudgrColors.Gray700
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = when (uiState.sessionStatus) {
                    SessionStatus.RUNNING -> "Session Running"
                    SessionStatus.PAUSED -> "Session Paused"
                    else -> "Ready to Focus"
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (uiState.sessionStatus == SessionStatus.OFF) {
                Text(
                    text = "Duration: ${uiState.plannedDurationText}",
                    fontSize = 14.sp,
                    color = NudgrColors.TextSecondary
                )
                
                Text(
                    text = "Interval: ${uiState.reminderIntervalText}",
                    fontSize = 14.sp,
                    color = NudgrColors.TextSecondary
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                NudgrButton(
                    text = "Start Session",
                    onClick = onStartSession,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.canStartSession,
                    variant = ButtonVariant.Primary,
                    hasGlow = uiState.canStartSession
                )
            } else {
                // Running session controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    NudgrButton(
                        text = if (uiState.sessionStatus == SessionStatus.PAUSED) "Resume" else "Pause",
                        onClick = onPauseResume,
                        modifier = Modifier.weight(1f),
                        variant = ButtonVariant.Secondary
                    )
                    
                    NudgrButton(
                        text = "End",
                        onClick = onEndSession,
                        modifier = Modifier.weight(1f),
                        variant = ButtonVariant.Error
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NudgrColors.Gray800.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = NudgrColors.Primary,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Text(
                text = title,
                fontSize = 12.sp,
                color = NudgrColors.Gray400
            )
        }
    }
}

@Composable
private fun ActionButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = NudgrColors.Gray800.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = NudgrColors.Primary,
                modifier = Modifier.size(20.dp)
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = text,
                fontSize = 12.sp,
                color = NudgrColors.Gray300
            )
        }
    }
}

@Composable
private fun LastSessionCard(
    session: com.nudgr.data.local.entity.Session,
    onViewReport: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NudgrColors.Gray800.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Last Session",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Duration: ${formatDuration(session.actualDurationMs)}",
                fontSize = 14.sp,
                color = NudgrColors.Gray400
            )
            
            Text(
                text = "Unlocks: ${session.totalUnlocks}",
                fontSize = 14.sp,
                color = NudgrColors.Gray400
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            TextButton(
                onClick = onViewReport,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = "View Report",
                    color = NudgrColors.Primary,
                    fontSize = 14.sp
                )
            }
        }
    }
}

private fun formatDuration(milliseconds: Long): String {
    val totalSeconds = milliseconds / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    
    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}
