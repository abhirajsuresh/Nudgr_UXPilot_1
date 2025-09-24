package com.nudgr.ui.checklist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.shadow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
fun ChecklistScreen(
    navController: NavController,
    viewModel: ChecklistViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.checkAllRequirements()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(NudgrGradients.Screen))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Text(
                text = "Setup Checklist",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = NudgrColors.TextPrimary,
                letterSpacing = 0.5.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Complete these steps to start your first focus session",
                fontSize = 16.sp,
                color = NudgrColors.TextSecondary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Checklist items
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                ChecklistItem(
                    title = "Add Images",
                    subtitle = "Add motivational images for reminders",
                    icon = Icons.Rounded.Image,
                    isCompleted = uiState.hasAtLeastOneImage,
                    onClick = { navController.navigate("image_library") }
                )
                
                ChecklistItem(
                    title = "Set Timers",
                    subtitle = "Configure session duration and reminder interval",
                    icon = Icons.Rounded.Schedule,
                    isCompleted = uiState.hasValidTimers,
                    onClick = { navController.navigate("timer_setup") }
                )
                
                ChecklistItem(
                    title = "Grant Permissions",
                    subtitle = "Allow notifications and system access",
                    icon = Icons.Rounded.Security,
                    isCompleted = uiState.hasCorePermissions,
                    onClick = { navController.navigate("permissions_hub") }
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Continue button
            NudgrButton(
                text = "Continue Setup",
                onClick = { navController.navigate("dashboard") },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.canContinue,
                variant = ButtonVariant.Primary,
                hasGlow = uiState.canContinue
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ChecklistItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .then(
                if (isCompleted) {
                    Modifier.shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = NudgrColors.Primary.copy(alpha = 0.3f),
                        spotColor = NudgrColors.Primary.copy(alpha = 0.5f)
                    )
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) {
                NudgrColors.Primary.copy(alpha = 0.1f)
            } else {
                NudgrColors.CardBg.copy(alpha = 0.8f)
            }
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isCompleted) 2.dp else 1.dp,
            color = if (isCompleted) NudgrColors.Primary else NudgrColors.BorderColor
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
                        brush = if (isCompleted) {
                            Brush.linearGradient(NudgrGradients.Accent)
                        } else {
                            Brush.linearGradient(listOf(NudgrColors.Gray700, NudgrColors.Gray600))
                        },
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = NudgrColors.Gray400,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            // Text content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isCompleted) NudgrColors.TextPrimary else NudgrColors.TextSecondary
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = if (isCompleted) NudgrColors.TextSecondary else NudgrColors.TextTertiary
                )
            }
            
            // Arrow indicator
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = if (isCompleted) NudgrColors.Primary else NudgrColors.Gray600,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
