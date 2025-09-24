package com.nudgr.ui.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nudgr.R
import com.nudgr.core.ui.components.NudgrButton
import com.nudgr.core.ui.components.ButtonVariant
import com.nudgr.core.ui.theme.NudgrColors
import com.nudgr.core.ui.theme.NudgrGradients

@Composable
fun WelcomeScreen(
    navController: NavController,
    viewModel: WelcomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.trackAppOpen()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(NudgrGradients.Screen))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Skip button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 48.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = { navController.navigate("checklist") }
                ) {
                    Text(
                        text = "SKIP",
                        color = NudgrColors.TextSecondary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.sp
                    )
                }
            }
            
            // Hero content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Illustration
                Box(
                    modifier = Modifier
                        .size(320.dp)
                        .background(
                            color = NudgrColors.Gray800,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(24.dp),
                            ambientColor = NudgrColors.Primary.copy(alpha = 0.2f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Placeholder for the meditation illustration
                    // TODO: Replace with actual illustration
                    Text(
                        text = "🧘‍♀️",
                        fontSize = 120.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(48.dp))
                
                // App name
                Text(
                    text = "NUDGR",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = NudgrColors.TextPrimary,
                    letterSpacing = 2.sp
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Tagline
                Text(
                    text = "RECLAIM YOUR ATTENTION",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = NudgrColors.TextSecondary,
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.5.sp
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Description
                Text(
                    text = "Transform phone distractions into gentle NUDGES that guide you back to DEEP WORK",
                    fontSize = 16.sp,
                    color = NudgrColors.TextTertiary,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                Spacer(modifier = Modifier.height(48.dp))
                
                // Progress dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Active dot
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                brush = Brush.linearGradient(NudgrGradients.Accent),
                                shape = CircleShape
                            )
                            .shadow(
                                elevation = 4.dp,
                                shape = CircleShape,
                                ambientColor = NudgrColors.Primary.copy(alpha = 0.4f)
                            )
                    )
                    
                    // Inactive dots
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                color = NudgrColors.Gray600,
                                shape = CircleShape
                            )
                    )
                    
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                color = NudgrColors.Gray600,
                                shape = CircleShape
                            )
                    )
                }
            }
            
            // Authentication buttons
            Column(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 48.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Google Sign-In
                Button(
                    onClick = { /* TODO: Implement Google Sign-In */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = NudgrColors.DarkBg
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Google icon placeholder
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .background(Color.Red, CircleShape)
                        )
                        Text(
                            text = "Continue with Google",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                
                // Email Sign-In
                Button(
                    onClick = { /* TODO: Implement Email Sign-In */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NudgrColors.Gray800,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = NudgrColors.Gray700
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Email icon placeholder
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .background(Color.White, CircleShape)
                        )
                        Text(
                            text = "Sign in with Email",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                
                // Continue as Guest
                Button(
                    onClick = { 
                        viewModel.onGuestContinue()
                        navController.navigate("checklist")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(16.dp),
                            ambientColor = NudgrColors.Primary.copy(alpha = 0.4f),
                            spotColor = NudgrColors.Primary.copy(alpha = 0.6f)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.linearGradient(NudgrGradients.Accent),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "CONTINUE AS GUEST",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
                
                // Terms and Privacy
                Text(
                    text = "By continuing, you agree to our Terms of Service and Privacy Policy",
                    fontSize = 12.sp,
                    color = NudgrColors.TextTertiary,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            
            // Version footer
            Text(
                text = "Version 1.0.2",
                fontSize = 12.sp,
                color = NudgrColors.TextTertiary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            )
        }
    }
}
