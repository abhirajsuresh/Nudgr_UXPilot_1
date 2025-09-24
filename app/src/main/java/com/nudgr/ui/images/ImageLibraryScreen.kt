package com.nudgr.ui.images

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.nudgr.core.ui.components.NudgrButton
import com.nudgr.core.ui.components.ButtonVariant
import com.nudgr.core.ui.theme.NudgrColors
import com.nudgr.core.ui.theme.NudgrGradients
import com.nudgr.data.local.entity.Image

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageLibraryScreen(
    navController: NavController,
    viewModel: ImageLibraryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        viewModel.importImages(uris)
    }
    
    LaunchedEffect(Unit) {
        viewModel.loadImages()
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
                    text = "Image Library",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = NudgrColors.TextPrimary,
                    letterSpacing = 0.5.sp
                )
                
                TextButton(
                    onClick = { imagePickerLauncher.launch("image/*") }
                ) {
                    Text(
                        text = "Add",
                        color = NudgrColors.Primary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Content
            if (uiState.images.isEmpty()) {
                // Empty state
                EmptyState(
                    onAddImages = { imagePickerLauncher.launch("image/*") }
                )
            } else {
                // Image grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(uiState.images) { image ->
                        ImageCard(
                            image = image,
                            onDelete = { viewModel.deleteImage(image) },
                            onReplace = { /* TODO: Implement replace */ }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Continue button
            NudgrButton(
                text = "Continue Setup",
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.images.isNotEmpty(),
                variant = ButtonVariant.Primary
            )
        }
    }
}

@Composable
private fun EmptyState(
    onAddImages: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    brush = Brush.linearGradient(listOf(NudgrColors.Gray700, NudgrColors.Gray600)),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Image,
                contentDescription = null,
                tint = NudgrColors.Gray400,
                modifier = Modifier.size(40.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "No images added yet",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Add your first motivational images to get started",
            fontSize = 14.sp,
            color = NudgrColors.Gray400,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        NudgrButton(
            text = "Add Images",
            onClick = onAddImages,
            variant = ButtonVariant.Primary,
            icon = Icons.Rounded.Add
        )
    }
}

@Composable
private fun ImageCard(
    image: Image,
    onDelete: () -> Unit,
    onReplace: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NudgrColors.Gray800)
    ) {
        Box {
            // Image
            AsyncImage(
                model = image.filePath,
                contentDescription = image.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Overlay with actions
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
            
            // Actions
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onReplace,
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = NudgrColors.Gray700.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Replace",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
                
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = NudgrColors.Error.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "Delete",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
