package com.nudgr.core.ui.theme

import androidx.compose.ui.graphics.Color

object NudgrColors {
    // Primary colors from design
    val DarkBg = Color(0xFF1F1F2E)
    val DarkBgSecondary = Color(0xFF2A2A3E)
    val Primary = Color(0xFF464EFF)
    val Secondary = Color(0xFF6C4EE3)
    
    // Status colors
    val Success = Color(0xFF10B981)
    val SuccessDark = Color(0xFF059669)
    val Warning = Color(0xFFF59E0B)
    val WarningDark = Color(0xFFD97706)
    val Error = Color(0xFFEF4444)
    val ErrorDark = Color(0xFFDC2626)
    
    // Gray scale
    val Gray900 = Color(0xFF111827)
    val Gray800 = Color(0xFF1F2937)
    val Gray700 = Color(0xFF374151)
    val Gray600 = Color(0xFF4B5563)
    val Gray500 = Color(0xFF6B7280)
    val Gray400 = Color(0xFF9CA3AF)
    val Gray300 = Color(0xFFD1D5DB)
    val Gray200 = Color(0xFFE5E7EB)
    val Gray100 = Color(0xFFF3F4F6)
    
    // Additional colors for UI elements
    val CardBg = Color(0xFF1F2937)
    val BorderColor = Color(0xFF374151)
    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFF9CA3AF)
    val TextTertiary = Color(0xFF6B7280)
}

object NudgrGradients {
    // Screen gradients
    val Screen = listOf(NudgrColors.DarkBg, NudgrColors.DarkBgSecondary)
    val Card = listOf(NudgrColors.Gray800, NudgrColors.Gray700)
    
    // Accent gradients
    val Accent = listOf(NudgrColors.Primary, NudgrColors.Secondary)
    val AccentGlow = listOf(NudgrColors.Primary.copy(alpha = 0.4f), NudgrColors.Secondary.copy(alpha = 0.4f))
    
    // Status gradients
    val Success = listOf(NudgrColors.Success, NudgrColors.SuccessDark)
    val Warning = listOf(NudgrColors.Warning, NudgrColors.WarningDark)
    val Error = listOf(NudgrColors.Error, NudgrColors.ErrorDark)
    
    // Special effects
    val Pulse = listOf(NudgrColors.Primary.copy(alpha = 0.3f), NudgrColors.Primary.copy(alpha = 0.1f))
    val Selection = listOf(NudgrColors.Primary.copy(alpha = 0.8f), NudgrColors.Primary.copy(alpha = 0.6f))
}
