package com.nudgr.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.nudgr.MainActivity
import com.nudgr.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class SessionEngineService : Service() {
    
    @Inject
    lateinit var sessionRepository: com.nudgr.data.repository.SessionRepository
    
    @Inject
    lateinit var imageRepository: com.nudgr.data.repository.ImageRepository
    
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var sessionJob: Job? = null
    
    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "session_engine_channel"
        private const val CHANNEL_NAME = "Session Engine"
    }
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.getStringExtra("action") ?: return START_NOT_STICKY
        
        when (action) {
            "start" -> {
                val durationMs = intent.getLongExtra("duration_ms", 0)
                val intervalMs = intent.getLongExtra("interval_ms", 0)
                startSession(durationMs, intervalMs)
            }
            "end" -> endSession()
            "pause" -> pauseSession()
            "resume" -> resumeSession()
            "screen_locked" -> onScreenLocked()
            "screen_unlocked" -> onScreenUnlocked()
            "boot_completed" -> onBootCompleted()
        }
        
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    private fun startSession(durationMs: Long, intervalMs: Long) {
        sessionJob?.cancel()
        
        sessionJob = serviceScope.launch {
            try {
                // Create session record
                val sessionId = java.util.UUID.randomUUID().toString()
                val session = com.nudgr.data.local.entity.Session(
                    id = sessionId,
                    plannedDurationMs = durationMs,
                    actualDurationMs = 0,
                    reminderIntervalMs = intervalMs,
                    startTime = java.util.Date(),
                    endTime = null,
                    status = com.nudgr.data.local.entity.SessionStatus.RUNNING
                )
                
                sessionRepository.insertSession(session)
                
                // Start foreground service
                startForeground(NOTIFICATION_ID, createNotification("Session Running"))
                
                // TODO: Implement session timer logic
                // TODO: Implement reminder logic
                // TODO: Implement image selection logic
                
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    private fun endSession() {
        sessionJob?.cancel()
        stopForeground(true)
        stopSelf()
    }
    
    private fun pauseSession() {
        // TODO: Implement pause logic
    }
    
    private fun resumeSession() {
        // TODO: Implement resume logic
    }
    
    private fun onScreenLocked() {
        // TODO: Implement screen lock logic
    }
    
    private fun onScreenUnlocked() {
        // TODO: Implement screen unlock logic
    }
    
    private fun onBootCompleted() {
        // TODO: Check for active sessions and restart if needed
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Session engine notifications"
                setShowBadge(false)
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun createNotification(contentText: String): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Nudgr")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
