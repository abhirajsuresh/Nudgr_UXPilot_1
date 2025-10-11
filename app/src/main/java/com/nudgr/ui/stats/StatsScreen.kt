package com.nudgr.ui.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.nudgr.core.ui.theme.NudgrGradients
import com.nudgr.core.ui.theme.NudgrColors

@Composable
fun StatsScreen(
    navController: NavController,
    viewModel: StatsViewModel = hiltViewModel()
) {
    val todayStats by viewModel.todayStats.collectAsState()
    val weeklyStats by viewModel.weeklyStats.collectAsState()
    val monthlyStats by viewModel.monthlyStats.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadStats()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(NudgrGradients.Screen))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Header
            Text(
                text = "Your Progress",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Track your deep work journey",
                fontSize = 16.sp,
                color = NudgrColors.Gray400
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Today Stats
            StatsCard(
                title = "Today",
                stats = todayStats,
                color = NudgrColors.Primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // This Week Stats
            StatsCard(
                title = "This Week",
                stats = weeklyStats,
                color = NudgrColors.Success
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // This Month Stats
            StatsCard(
                title = "This Month",
                stats = monthlyStats,
                color = NudgrColors.Info
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun StatsCard(
    title: String,
    stats: com.nudgr.data.repository.SessionRepository.SessionStats,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(color, shape = RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Main Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    label = "Sessions",
                    value = stats.totalSessions.toString(),
                    icon = "📊"
                )
                StatItem(
                    label = "Minutes",
                    value = stats.totalDurationMinutes.toString(),
                    icon = "⏱️"
                )
                StatItem(
                    label = "Avg/Session",
                    value = "${stats.averageSessionMinutes}m",
                    icon = "📈"
                )
            }
            
            if (stats.totalSessions > 0) {
                Spacer(modifier = Modifier.height(16.dp))
                
                Divider(color = Color.White.copy(alpha = 0.2f))
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Secondary Stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SecondaryStatItem(
                        label = "Nudges",
                        value = stats.totalReminders.toString()
                    )
                    SecondaryStatItem(
                        label = "Unlocks",
                        value = stats.totalUnlocks.toString()
                    )
                    if (stats.totalSnoozes > 0) {
                        SecondaryStatItem(
                            label = "Snoozes",
                            value = stats.totalSnoozes.toString()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatItem(
    label: String,
    value: String,
    icon: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = NudgrColors.Gray400
        )
    }
}

@Composable
fun SecondaryStatItem(
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = NudgrColors.Gray400
        )
    }
}

