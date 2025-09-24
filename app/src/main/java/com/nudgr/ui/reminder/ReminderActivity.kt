package com.nudgr.ui.reminder

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import coil.compose.AsyncImage
import com.nudgr.core.ui.components.NudgrButton
import com.nudgr.core.ui.components.ButtonVariant
import com.nudgr.core.ui.theme.NudgrColors
import com.nudgr.core.ui.theme.NudgrGradients
import com.nudgr.core.ui.theme.NudgrTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReminderActivity : ComponentActivity() {
    
    private val viewModel: ReminderViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Make this activity appear over lockscreen
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setShowWhenLocked(true)
        setTurnScreenOn(true)
        
        setContent {
            NudgrTheme {
                ReminderScreen(
                    viewModel = viewModel,
                    onDismiss = { finish() }
                )
            }
        }
    }
}

@Composable
fun ReminderScreen(
    viewModel: ReminderViewModel,
    onDismiss: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadRandomImage()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(NudgrGradients.Screen))
    ) {
        // Background image
        if (uiState.imagePath != null) {
            AsyncImage(
                model = uiState.imagePath,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        
        // Dark overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                )
        )
        
        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Main message
            Text(
                text = "Time to refocus",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Action buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Lock Phone button (if device admin enabled)
                if (uiState.deviceAdminEnabled) {
                    NudgrButton(
                        text = "Lock Phone",
                        onClick = { 
                            viewModel.lockPhone()
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        variant = ButtonVariant.Error
                    )
                }
                
                // Snooze button
                NudgrButton(
                    text = "Snooze",
                    onClick = { 
                        viewModel.snooze()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    variant = ButtonVariant.Secondary
                )
                
                // Extend button
                NudgrButton(
                    text = "Extend (+5 min)",
                    onClick = { 
                        viewModel.extend()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    variant = ButtonVariant.Primary
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Dismiss button
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "Dismiss",
                    color = NudgrColors.Gray400,
                    fontSize = 16.sp
                )
            }
        }
    }
}
