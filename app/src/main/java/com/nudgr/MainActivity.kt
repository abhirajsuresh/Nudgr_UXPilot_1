package com.nudgr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nudgr.core.ui.theme.NudgrTheme
import com.nudgr.ui.welcome.WelcomeScreen
import com.nudgr.ui.checklist.ChecklistScreen
import com.nudgr.ui.images.ImageLibraryScreen
import com.nudgr.ui.timers.TimerSetupScreen
import com.nudgr.ui.dashboard.DashboardScreen
import com.nudgr.ui.permissions.PermissionsHubScreen
import com.nudgr.ui.session_summary.SessionSummaryScreen
import com.nudgr.ui.stats.StatsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            NudgrTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    
                    NavHost(
                        navController = navController,
                        startDestination = "welcome"
                    ) {
                        composable("welcome") {
                            WelcomeScreen(navController = navController)
                        }
                        
                        composable("checklist") {
                            ChecklistScreen(navController = navController)
                        }
                        
                        composable("image_library") {
                            ImageLibraryScreen(navController = navController)
                        }
                        
                        composable("timer_setup") {
                            TimerSetupScreen(navController = navController)
                        }
                        
                        composable("dashboard") {
                            DashboardScreen(navController = navController)
                        }
                        
                        composable("permissions_hub") {
                            PermissionsHubScreen(navController = navController)
                        }

                        composable("session_summary/{sessionId}") { backStackEntry ->
                            SessionSummaryScreen(
                                navController = navController,
                                sessionId = backStackEntry.arguments?.getString("sessionId")
                            )
                        }
                        
                        composable("stats") {
                            StatsScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}
