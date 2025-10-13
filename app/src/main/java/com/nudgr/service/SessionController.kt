package com.nudgr.service

import android.content.Context
import android.content.Intent
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionController @Inject constructor(
    @ApplicationContext private val context: Context
) : DefaultLifecycleObserver {
    
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    fun startSession(durationMs: Long, intervalMs: Long) {
        scope.launch {
            val intent = Intent(context, SessionEngineService::class.java).apply {
                putExtra("action", "start")
                putExtra("duration_ms", durationMs)
                putExtra("interval_ms", intervalMs)
            }
            context.startForegroundService(intent)
        }
    }
    
    fun endSession() {
        scope.launch {
            val intent = Intent(context, SessionEngineService::class.java).apply {
                putExtra("action", "end")
            }
            context.startService(intent)
        }
    }
    
    fun pauseSession() {
        scope.launch {
            val intent = Intent(context, SessionEngineService::class.java).apply {
                putExtra("action", "pause")
            }
            context.startService(intent)
        }
    }
    
    fun resumeSession() {
        scope.launch {
            val intent = Intent(context, SessionEngineService::class.java).apply {
                putExtra("action", "resume")
            }
            context.startService(intent)
        }
    }
    
    fun snoozeNudge(delayMs: Long = 5 * 60 * 1000L) {
        scope.launch {
            val intent = Intent(context, SessionEngineService::class.java).apply {
                putExtra("action", "snooze")
                putExtra("snooze_delay_ms", delayMs)
            }
            context.startService(intent)
        }
    }
    
    fun extendSession(extensionMs: Long = 5 * 60 * 1000L) {
        scope.launch {
            val intent = Intent(context, SessionEngineService::class.java).apply {
                putExtra("action", "extend")
                putExtra("extension_ms", extensionMs)
            }
            context.startService(intent)
        }
    }
    
    fun onScreenLocked() {
        scope.launch {
            val intent = Intent(context, SessionEngineService::class.java).apply {
                putExtra("action", "screen_locked")
            }
            context.startService(intent)
        }
    }
    
    fun onScreenUnlocked() {
        scope.launch {
            val intent = Intent(context, SessionEngineService::class.java).apply {
                putExtra("action", "screen_unlocked")
            }
            context.startService(intent)
        }
    }
    
    fun onBootCompleted() {
        scope.launch {
            // Check if there's an active session and restart service if needed
            val intent = Intent(context, SessionEngineService::class.java).apply {
                putExtra("action", "boot_completed")
            }
            context.startService(intent)
        }
    }
    
    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        scope.cancel()
    }
}
