package com.nudgr.receiver

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DeviceAdminReceiver : DeviceAdminReceiver() {
    
    @Inject
    lateinit var sessionController: com.nudgr.service.SessionController
    
    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        // Device admin enabled
        // TODO: Track nudgr_device_admin_enabled event
    }
    
    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
        // Device admin disabled
        // TODO: Track nudgr_device_admin_disabled event
    }
    
    override fun onDisableRequested(context: Context, intent: Intent): CharSequence {
        return "Disabling device admin will prevent the lock phone feature from working."
    }
}
