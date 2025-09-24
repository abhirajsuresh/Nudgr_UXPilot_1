package com.nudgr.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nudgr.core.ui.theme.NudgrColors
import com.nudgr.core.ui.theme.NudgrGradients

@Composable
fun NudgrButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: ButtonVariant = ButtonVariant.Primary,
    icon: ImageVector? = null,
    iconPosition: IconPosition = IconPosition.Start,
    hasGlow: Boolean = false
) {
    val (backgroundBrush, textColor, glowColor) = when (variant) {
        ButtonVariant.Primary -> Triple(
            Brush.linearGradient(NudgrGradients.Accent),
            Color.White,
            NudgrColors.Primary.copy(alpha = 0.4f)
        )
        ButtonVariant.Secondary -> Triple(
            Brush.linearGradient(NudgrGradients.Card),
            Color.White,
            NudgrColors.Gray600.copy(alpha = 0.3f)
        )
        ButtonVariant.Success -> Triple(
            Brush.linearGradient(NudgrGradients.Success),
            Color.White,
            NudgrColors.Success.copy(alpha = 0.4f)
        )
        ButtonVariant.Warning -> Triple(
            Brush.linearGradient(NudgrGradients.Warning),
            Color.White,
            NudgrColors.Warning.copy(alpha = 0.4f)
        )
        ButtonVariant.Error -> Triple(
            Brush.linearGradient(NudgrGradients.Error),
            Color.White,
            NudgrColors.Error.copy(alpha = 0.4f)
        )
        ButtonVariant.Outline -> Triple(
            Brush.linearGradient(listOf(Color.Transparent, Color.Transparent)),
            NudgrColors.Primary,
            NudgrColors.Primary.copy(alpha = 0.2f)
        )
    }

    val buttonModifier = if (hasGlow && enabled) {
        modifier
            .height(56.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = glowColor,
                spotColor = glowColor
            )
    } else {
        modifier.height(56.dp)
    }

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = buttonModifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = textColor,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = NudgrColors.Gray400
        ),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = if (enabled) backgroundBrush else Brush.linearGradient(NudgrGradients.Card),
                    shape = RoundedCornerShape(16.dp)
                )
                .then(
                    if (variant == ButtonVariant.Outline && enabled) {
                        Modifier.border(
                            width = 1.dp,
                            color = NudgrColors.Primary,
                            shape = RoundedCornerShape(16.dp)
                        )
                    } else {
                        Modifier
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null && iconPosition == IconPosition.Start) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = textColor
                    )
                }
                
                Text(
                    text = text,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor
                )
                
                if (icon != null && iconPosition == IconPosition.End) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = textColor
                    )
                }
            }
        }
    }
}

enum class ButtonVariant {
    Primary, Secondary, Success, Warning, Error, Outline
}

enum class IconPosition {
    Start, End
}
