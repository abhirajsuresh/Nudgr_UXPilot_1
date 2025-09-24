package com.nudgr.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "session_events")
data class SessionEvent(
    @PrimaryKey val id: String,
    val sessionId: String,
    val eventType: SessionEventType,
    val timestamp: Date,
    val data: String? = null // JSON for additional event data
)

enum class SessionEventType {
    SESSION_STARTED,
    SESSION_PAUSED,
    SESSION_RESUMED,
    SESSION_ENDED,
    SESSION_CANCELLED,
    SCREEN_UNLOCKED,
    SCREEN_LOCKED,
    REMINDER_SHOWN,
    ACTION_LOCK,
    ACTION_SNOOZE,
    ACTION_EXTEND,
    IMAGE_SELECTED
}
