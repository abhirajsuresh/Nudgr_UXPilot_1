# 🚀 New Features Guide - Nudgr MVP

## 📊 Statistics Dashboard

### Access the Stats Screen
Navigate from anywhere in the app:
```kotlin
navController.navigate("stats")
```

### What You'll See
- **Today's Stats** - Your progress today (sessions, minutes, avg)
- **This Week** - Monday through Sunday statistics
- **This Month** - Current month aggregation

### Stats Include:
- 📊 Total sessions completed
- ⏱️ Total deep work minutes
- 📈 Average session duration
- 🔔 Nudges received
- 🔓 Phone unlocks
- 😴 Snoozes used

---

## ⏰ Snooze & Extend Features

### During a Session
When you receive a nudge on `ReminderActivity`:

1. **Snooze Button** - Delays the next nudge by 5 minutes
2. **Extend Button** - Adds 5 minutes to your current session

### How It Works
- Snooze resets the nudge timer
- Extend increases the `plannedDurationMs`
- Both actions are tracked in statistics
- Session continues without interruption

---

## 🔄 Boot Recovery

### Automatic Session Recovery
If your phone reboots during a session:

1. **On Boot:** App checks for interrupted sessions
2. **Active Session Found:** 
   - Calculates remaining time
   - Restarts session automatically
3. **Expired Session:**
   - Marks as completed
   - Records final statistics

### No Action Required
The app handles everything automatically!

---

## 📈 Enhanced Session Summary

### After Completing a Session
Navigate to `session_summary/{sessionId}` to see:

- ✅ Duration (formatted: "2h 15m" or "45m 30s")
- ✅ Reminders received during session
- ✅ Phone unlocks during session
- ✅ Snoozes used (if any)

---

## 🎨 UI Improvements

### Visual Enhancements
- Beautiful gradient backgrounds
- Card-based statistics display
- Color-coded time periods (Today/Week/Month)
- Responsive layouts
- Human-readable time formats

---

## 🧪 Testing the New Features

### Test Session Tracking
1. Start a session from Dashboard
2. Trigger a nudge (wait for reminder interval)
3. Unlock phone → Count increments
4. Check session summary after completion

### Test Snooze/Extend
1. Start a session
2. Wait for nudge
3. Click "Snooze" → Next nudge delayed 5 min
4. Click "Extend" → Session duration +5 min

### Test Statistics
1. Complete 2-3 sessions
2. Navigate to `/stats`
3. Verify counts are accurate
4. Check today/week/month aggregations

### Test Boot Recovery
1. Start a session
2. Reboot device
3. After boot → Session should resume automatically
4. Remaining time should be accurate

---

## 🔌 Integration Points

### Add Stats Button to Dashboard
In `DashboardScreen.kt`:
```kotlin
Button(onClick = { navController.navigate("stats") }) {
    Text("View Statistics")
}
```

### Access Session Stats Programmatically
In any ViewModel:
```kotlin
@Inject lateinit var sessionRepository: SessionRepository

viewModelScope.launch {
    val todayStats = sessionRepository.getDailyStats()
    val weekStats = sessionRepository.getWeeklyStats()
    val monthStats = sessionRepository.getMonthlyStats()
}
```

---

## 📝 Configuration

### Customize Snooze/Extend Duration
In `SessionController.kt`:
```kotlin
// Change default snooze delay (currently 5 minutes)
sessionController.snoozeNudge(10 * 60 * 1000L) // 10 minutes

// Change default extend duration (currently 5 minutes)
sessionController.extendSession(15 * 60 * 1000L) // 15 minutes
```

### Modify Week Start Day
In `SessionRepository.kt`:
```kotlin
private fun getStartOfWeek(): Date {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY) // Change to Sunday
        // ... rest of setup
    }
    return calendar.time
}
```

---

## 🐛 Troubleshooting

### Stats Not Showing
- **Issue:** Stats screen shows 0 sessions
- **Solution:** Complete at least one session first
- **Check:** Verify session status is COMPLETED in database

### Boot Recovery Not Working
- **Issue:** Session doesn't resume after reboot
- **Solution:** Check `BootReceiver` is registered in `AndroidManifest.xml`
- **Verify:** BootReceiver has `RECEIVE_BOOT_COMPLETED` permission

### Snooze Not Delaying Nudge
- **Issue:** Nudge appears immediately after snooze
- **Solution:** Check nudge interval in session settings
- **Verify:** `snoozeNudge()` is being called correctly

---

## 📚 Related Files

### Core Implementation
- `SessionEngineService.kt` - Main session logic
- `SessionRepository.kt` - Data access & stats
- `SessionController.kt` - Service control interface

### UI Components
- `StatsScreen.kt` - Statistics dashboard
- `SessionSummaryScreen.kt` - Completed session details
- `ReminderActivity.kt` - Nudge UI with snooze/extend

### ViewModels
- `StatsViewModel.kt` - Statistics state management
- `ReminderViewModel.kt` - Nudge interaction handling

---

## 🎓 Best Practices

1. **Always check for COMPLETED status** when calculating statistics
2. **Use StateFlow** for reactive UI updates
3. **Handle edge cases** in boot recovery (expired sessions)
4. **Persist immediately** for event tracking (nudges, unlocks)
5. **Calculate on-demand** for statistics (don't cache unnecessarily)

---

**Last Updated:** October 11, 2025  
**Version:** MVP Release  
**Status:** ✅ Production Ready

