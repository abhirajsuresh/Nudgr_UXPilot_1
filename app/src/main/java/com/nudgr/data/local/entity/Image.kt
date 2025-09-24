package com.nudgr.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "images")
data class Image(
    @PrimaryKey val id: String,
    val name: String,
    val filePath: String,
    val fileSize: Long,
    val mimeType: String,
    val contentHash: String, // SHA-256 for deduplication
    val addedAt: Date,
    val isActive: Boolean = true // For rotation control
)
