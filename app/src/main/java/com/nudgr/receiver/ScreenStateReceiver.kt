package com.nudgr.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ScreenStateReceiver : BroadcastReceiver() {
    
    @Inject
    lateinit var sessionController: com.nudgr.service.SessionController
    
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_SCREEN_OFF -> {
                // Screen locked - pause session countdown
                sessionController.onScreenLocked()
            }
            Intent.ACTION_USER_PRESENT -> {
                // Screen unlocked - resume session countdown
                sessionController.onScreenUnlocked()
            }
        }
    }
}
