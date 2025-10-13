package com.nudgr.ui.timers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nudgr.core.ui.components.NudgrButton
import com.nudgr.core.ui.components.ButtonVariant
import com.nudgr.core.ui.theme.NudgrColors
import com.nudgr.core.ui.theme.NudgrGradients

@Composable
fun TimerSetupScreen(
    navController: NavController,
    viewModel: TimerSetupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
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
                    text = "Timer Setup",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = NudgrColors.TextPrimary,
                    letterSpacing = 0.5.sp
                )
                
                TextButton(
                    onClick = { viewModel.resetToDefaults() }
                ) {
                    Text(
                        text = "Reset",
                        color = NudgrColors.Primary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Deep Work Duration
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NudgrColors.Gray800.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Deep Work Duration",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Hours
                        Column {
                            Text(
                                text = "Hours",
                                fontSize = 14.sp,
                                color = NudgrColors.Gray400
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = uiState.durationHours.toString(),
                                onValueChange = { viewModel.onDurationHoursChanged(it) },
                                modifier = Modifier.width(80.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = NudgrColors.Primary,
                                    unfocusedBorderColor = NudgrColors.Gray600
                                ),
                                singleLine = true
                            )
                        }
                        
                        Text(
                            text = ":",
                            fontSize = 24.sp,
                            color = NudgrColors.Gray400,
                            fontWeight = FontWeight.Bold
                        )
                        
                        // Minutes
                        Column {
                            Text(
                                text = "Minutes",
                                fontSize = 14.sp,
                                color = NudgrColors.Gray400
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = uiState.durationMinutes.toString(),
                                onValueChange = { viewModel.updateDurationMinutes(it.toIntOrNull() ?: 0) },
                                modifier = Modifier.width(80.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = NudgrColors.Primary,
                                    unfocusedBorderColor = NudgrColors.Gray600
                                ),
                                singleLine = true
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Reminder Interval
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NudgrColors.Gray800.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Reminder Interval",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Minutes
                        Column {
                            Text(
                                text = "Minutes",
                                fontSize = 14.sp,
                                color = NudgrColors.Gray400
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = uiState.intervalMinutes.toString(),
                                onValueChange = { viewModel.updateIntervalMinutes(it.toIntOrNull() ?: 0) },
                                modifier = Modifier.width(80.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = NudgrColors.Primary,
                                    unfocusedBorderColor = NudgrColors.Gray600
                                ),
                                singleLine = true
                            )
                        }
                        
                        Text(
                            text = ":",
                            fontSize = 24.sp,
                            color = NudgrColors.Gray400,
                            fontWeight = FontWeight.Bold
                        )
                        
                        // Seconds
                        Column {
                            Text(
                                text = "Seconds",
                                fontSize = 14.sp,
                                color = NudgrColors.Gray400
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = uiState.intervalSeconds.toString(),
                                onValueChange = { viewModel.updateIntervalSeconds(it.toIntOrNull() ?: 0) },
                                modifier = Modifier.width(80.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = NudgrColors.Primary,
                                    unfocusedBorderColor = NudgrColors.Gray600
                                ),
                                singleLine = true
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Validation message
            if (uiState.validationMessage.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = NudgrColors.Error.copy(alpha = 0.1f))
                ) {
                    Text(
                        text = uiState.validationMessage,
                        modifier = Modifier.padding(16.dp),
                        color = NudgrColors.Error,
                        fontSize = 14.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Save button
            NudgrButton(
                text = "Save Timers",
                onClick = { 
                    viewModel.saveTimers()
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.isValid,
                variant = ButtonVariant.Primary
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
