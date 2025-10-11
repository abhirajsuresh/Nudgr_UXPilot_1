package com.nudgr.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.nudgr.MainActivity
import com.nudgr.R
import com.nudgr.analytics.AnalyticsHelper
import com.nudgr.ui.reminder.ReminderActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class SessionEngineService : Service() {
    
    @Inject
    lateinit var sessionRepository: com.nudgr.data.repository.SessionRepository
    
    @Inject
    lateinit var imageRepository: com.nudgr.data.repository.ImageRepository
    
    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var sessionJob: Job? = null
    private var wakeLock: PowerManager.WakeLock? = null
    
    // In-memory flag to track session running state (observable by the loop)
    @Volatile
    private var isSessionRunning: Boolean = false
    
    companion object {
        private const val NOTIFICATION_ID = 1
        private const val NOTIFICATION_ID_NUDGE = 2
        private const val CHANNEL_ID = "session_engine_channel"
        private const val CHANNEL_NAME = "Session Engine"
        private const val DB_BATCH_INTERVAL_MS = 3000L // Write to DB every 3 seconds
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
            "snooze" -> {
                val delayMs = intent.getLongExtra("snooze_delay_ms", 5 * 60 * 1000L)
                snoozeNudge(delayMs)
            }
            "extend" -> {
                val extensionMs = intent.getLongExtra("extension_ms", 5 * 60 * 1000L)
                extendSession(extensionMs)
            }
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
                var session = com.nudgr.data.local.entity.Session(
                    id = sessionId,
                    plannedDurationMs = durationMs,
                    actualDurationMs = 0,
                    reminderIntervalMs = intervalMs,
                    startTime = java.util.Date(),
                    endTime = null,
                    status = com.nudgr.data.local.entity.SessionStatus.RUNNING
                )
                
                sessionRepository.insertSession(session)
                
                // Set in-memory flag
                isSessionRunning = true
                
                // Acquire WakeLock to prevent device sleep during session
                val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
                wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Nudgr::SessionWakeLock")
                wakeLock?.setReferenceCounted(false)
                wakeLock?.acquire(durationMs)
                
                // Start foreground service
                startForeground(NOTIFICATION_ID, createNotification("Session Running"))
                
                var remainingTimeMs = durationMs
                var timeUntilNextNudgeMs = intervalMs
                var lastDbUpdateTimestamp = System.currentTimeMillis()
                
                while (remainingTimeMs > 0) {
                    delay(250) // Tick every 250ms
                    
                    // Reload session from DB to observe status changes
                    session = sessionRepository.getLatestSession() ?: session
                    
                    if (isSessionRunning && session.status == com.nudgr.data.local.entity.SessionStatus.RUNNING) {
                        remainingTimeMs -= 250
                        timeUntilNextNudgeMs -= 250
                        session.actualDurationMs += 250
                        
                        // Update notification with remaining time (UI update - stays at 250ms)
                        updateNotification("Session Running: ${formatTime(remainingTimeMs)}")
                        
                        // Check if it's time for a nudge
                        if (timeUntilNextNudgeMs <= 0) {
                            // Trigger a nudge
                            triggerNudge()
                            timeUntilNextNudgeMs = intervalMs // Reset nudge timer
                            
                            // Persist immediately on nudge event
                            sessionRepository.updateSession(session)
                            lastDbUpdateTimestamp = System.currentTimeMillis()
                        } else {
                            // Batch DB updates: only persist when threshold is met
                            val currentTime = System.currentTimeMillis()
                            if (currentTime - lastDbUpdateTimestamp >= DB_BATCH_INTERVAL_MS) {
                                sessionRepository.updateSession(session)
                                lastDbUpdateTimestamp = currentTime
                            }
                        }
                    } else {
                        // Status changed (paused/stopped) - persist immediately
                        val currentTime = System.currentTimeMillis()
                        if (currentTime - lastDbUpdateTimestamp >= 250) { // Avoid duplicate writes
                            sessionRepository.updateSession(session)
                            lastDbUpdateTimestamp = currentTime
                        }
                    }
                }
                
                // Ensure final state is persisted before ending
                sessionRepository.updateSession(session)
                isSessionRunning = false
                endSession()
                
            } catch (e: Exception) {
                e.printStackTrace()
                isSessionRunning = false
            }
        }
    }
    
    private fun triggerNudge() {
        analyticsHelper.trackEvent(AnalyticsHelper.Event.NUDGE_SHOWN)
        
        // Update session reminder count
        serviceScope.launch {
            val session = sessionRepository.getLatestSession()
            session?.let {
                val updated = it.copy(totalReminders = it.totalReminders + 1)
                sessionRepository.updateSession(updated)
            }
        }
        
        val intent = Intent(this, ReminderActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this, 1, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val nudgeNotification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Time to refocus!")
            .setContentText("A gentle nudge to get back on track.")
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setFullScreenIntent(pendingIntent, true)
            .build()
            
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID_NUDGE, nudgeNotification)
    }
    
    private fun formatTime(millis: Long): String {
        val hours = (millis / (1000 * 60 * 60)) % 24
        val minutes = (millis / (1000 * 60)) % 60
        val seconds = (millis / 1000) % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
    
    private fun endSession() {
        sessionJob?.cancel()
        
        // Release WakeLock safely
        if (wakeLock?.isHeld == true) {
            wakeLock?.release()
        }
        wakeLock = null
        
        // Use runBlocking to ensure session is marked COMPLETED and analytics are sent
        // before stopping the service
        runBlocking {
            val session = sessionRepository.getLatestSession()
            session?.let {
                it.status = com.nudgr.data.local.entity.SessionStatus.COMPLETED
                it.endTime = java.util.Date()
                sessionRepository.updateSession(it)
                analyticsHelper.trackEvent(AnalyticsHelper.Event.SESSION_COMPLETE)
            }
        }
        
        // API level check for stopForeground
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_DETACH)
        } else {
            @Suppress("DEPRECATION")
            stopForeground(true)
        }
        stopSelf()
    }
    
    private fun pauseSession() {
        // Set in-memory flag immediately so loop observes the change
        isSessionRunning = false
        
        serviceScope.launch {
            val session = sessionRepository.getLatestSession()
            session?.let {
                if (it.status == com.nudgr.data.local.entity.SessionStatus.RUNNING) {
                    it.status = com.nudgr.data.local.entity.SessionStatus.PAUSED
                    sessionRepository.updateSession(it)
                    updateNotification("Session Paused")
                }
            }
        }
    }
    
    private fun resumeSession() {
        // Set in-memory flag immediately so loop observes the change
        isSessionRunning = true
        
        serviceScope.launch {
            val session = sessionRepository.getLatestSession()
            session?.let {
                if (it.status == com.nudgr.data.local.entity.SessionStatus.PAUSED) {
                    it.status = com.nudgr.data.local.entity.SessionStatus.RUNNING
                    sessionRepository.updateSession(it)
                    updateNotification("Session Running")
                }
            }
        }
    }
    
    private fun onScreenLocked() {
        pauseSession()
    }
    
    private fun onScreenUnlocked() {
        // Track unlock event
        serviceScope.launch {
            val session = sessionRepository.getLatestSession()
            session?.let {
                val updated = it.copy(totalUnlocks = it.totalUnlocks + 1)
                sessionRepository.updateSession(updated)
            }
        }
        
        resumeSession()
    }
    
    private fun snoozeNudge(delayMs: Long) {
        // Reset the next nudge time by the delay amount
        // This is handled automatically by the ticker loop's interval tracking
        
        serviceScope.launch {
            val session = sessionRepository.getLatestSession()
            session?.let {
                val updated = it.copy(totalSnoozes = it.totalSnoozes + 1)
                sessionRepository.updateSession(updated)
            }
        }
        
        updateNotification("Nudge snoozed - next in ${delayMs / 60000} min")
    }
    
    private fun extendSession(extensionMs: Long) {
        serviceScope.launch {
            val session = sessionRepository.getLatestSession()
            session?.let {
                val updated = it.copy(
                    plannedDurationMs = it.plannedDurationMs + extensionMs
                )
                sessionRepository.updateSession(updated)
            }
        }
        
        updateNotification("Session extended by ${extensionMs / 60000} min")
    }
    
    private fun onBootCompleted() {
        serviceScope.launch {
            val session = sessionRepository.getCurrentSession()
            if (session != null && session.status in listOf(
                    com.nudgr.data.local.entity.SessionStatus.RUNNING,
                    com.nudgr.data.local.entity.SessionStatus.PAUSED
                )) {
                val elapsedMs = session.actualDurationMs
                val remainingMs = session.plannedDurationMs - elapsedMs
                
                if (remainingMs > 0) {
                    // Restart session with remaining time
                    startSession(remainingMs, session.reminderIntervalMs)
                } else {
                    // Session should have ended
                    val completed = session.copy(
                        status = com.nudgr.data.local.entity.SessionStatus.COMPLETED,
                        endTime = java.util.Date()
                    )
                    sessionRepository.updateSession(completed)
                    
                    // Track completion in analytics
                    analyticsHelper.trackEvent(AnalyticsHelper.Event.SESSION_COMPLETE)
                }
            }
        }
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
    
    private fun updateNotification(contentText: String) {
        val notification = createNotification(contentText)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        
        // Release WakeLock to prevent leaks
        if (wakeLock?.isHeld == true) {
            wakeLock?.release()
        }
        wakeLock = null
    }
}
