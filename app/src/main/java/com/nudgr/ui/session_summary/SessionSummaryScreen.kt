package com.nudgr.ui.session_summary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nudgr.core.ui.components.NudgrButton
import com.nudgr.core.ui.theme.NudgrGradients
import com.nudgr.core.ui.theme.NudgrTheme

@Composable
fun SessionSummaryScreen(
    navController: NavController,
    sessionId: String?,
    viewModel: SessionSummaryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(sessionId) {
        sessionId?.let { viewModel.loadSession(it) }
    }

    NudgrTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.linearGradient(NudgrGradients.Screen)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(32.dp)
            ) {
                Text(
                    "Session Complete!",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(32.dp))

                when (uiState) {
                    is SessionSummaryUiState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    is SessionSummaryUiState.Success -> {
                        val session = (uiState as SessionSummaryUiState.Success).session
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                "Duration: ${formatDuration(session.actualDurationMs)}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                "Reminders: ${session.totalReminders}",
                                fontSize = 18.sp
                            )
                            Text(
                                "Unlocks: ${session.totalUnlocks}",
                                fontSize = 18.sp
                            )
                            if (session.totalSnoozes > 0) {
                                Text(
                                    "Snoozes: ${session.totalSnoozes}",
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                    is SessionSummaryUiState.Error -> {
                        Text(
                            text = (uiState as SessionSummaryUiState.Error).message,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                NudgrButton(
                    text = "Back to Dashboard",
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

private fun formatDuration(millis: Long): String {
    val totalSeconds = millis / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    
    return when {
        hours > 0 -> String.format("%dh %dm", hours, minutes)
        minutes > 0 -> String.format("%dm %ds", minutes, seconds)
        else -> String.format("%ds", seconds)
    }
}
