package com.nudgr.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "sessions")
data class Session(
    @PrimaryKey val id: String,
    val plannedDurationMs: Long,
    val actualDurationMs: Long,
    val reminderIntervalMs: Long,
    val startTime: Date,
    val endTime: Date?,
    val status: SessionStatus,
    val totalUnlocks: Int = 0,
    val totalReminders: Int = 0,
    val totalSnoozes: Int = 0,
    val totalLocks: Int = 0
)

enum class SessionStatus {
    OFF, RUNNING, PAUSED, COMPLETED, CANCELLED
}
