# 🔧 Critical Fixes Needed Before MVP Testing

## ✅ Already Fixed
- [x] Added `getLatestSession()` method to SessionRepository (was called by service but missing)

---

## 🔴 HIGH PRIORITY - Fix These Next

### 1. Session Event Tracking Not Being Persisted
**File:** `app/src/main/java/com/nudgr/service/SessionEngineService.kt`

**Problem:** The service tracks nudges and unlocks but doesn't update the Session entity's counters.

**Add these changes:**

```kotlin
// In triggerNudge() method, after line 167 (after analytics tracking):
private fun triggerNudge() {
    analyticsHelper.trackEvent(AnalyticsHelper.Event.NUDGE_SHOWN)
    
    // ADD THIS: Update session reminder count
    serviceScope.launch {
        val session = sessionRepository.getLatestSession()
        session?.let {
            val updated = it.copy(totalReminders = it.totalReminders + 1)
            sessionRepository.updateSession(updated)
        }
    }
    
    // ... rest of existing code ...
}

// In onScreenUnlocked() method, update to track unlocks:
private fun onScreenUnlocked() {
    // ADD THIS: Track unlock event
    serviceScope.launch {
        val session = sessionRepository.getLatestSession()
        session?.let {
            val updated = it.copy(totalUnlocks = it.totalUnlocks + 1)
            sessionRepository.updateSession(updated)
        }
    }
    
    resumeSession()
}
```

**Impact if not fixed:** Session summaries will show 0 nudges and 0 unlocks.

---

### 2. Verify ReminderViewModel Exists and Works
**Expected File:** `app/src/main/java/com/nudgr/ui/reminder/ReminderViewModel.kt`

**Need to verify it has:**
```kotlin
@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
    // Other dependencies
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ReminderUiState())
    val uiState: StateFlow<ReminderUiState> = _uiState
    
    fun loadRandomImage() {
        viewModelScope.launch {
            val randomImageUri = imageRepository.getRandomImageUri()
            _uiState.value = _uiState.value.copy(imagePath = randomImageUri?.toString())
        }
    }
    
    fun lockPhone() {
        // Use DevicePolicyManager to lock device
    }
    
    fun snooze() {
        // TODO: Delay next nudge
    }
    
    fun extend() {
        // TODO: Extend session by 5 minutes
    }
}

data class ReminderUiState(
    val imagePath: String? = null,
    val deviceAdminEnabled: Boolean = false
)
```

**Action:** Check if this file exists and is complete. If not, create it.

---

## ⚠️ MEDIUM PRIORITY - Should Fix Before MVP

### 3. Boot Recovery Not Implemented
**File:** `app/src/main/java/com/nudgr/service/SessionEngineService.kt:269`

**Current code:**
```kotlin
private fun onBootCompleted() {
    // TODO: Check for active sessions and restart if needed
}
```

**Replace with:**
```kotlin
private fun onBootCompleted() {
    serviceScope.launch {
        val session = sessionRepository.getCurrentSession()
        if (session != null && session.status in listOf(SessionStatus.RUNNING, SessionStatus.PAUSED)) {
            val elapsedMs = session.actualDurationMs
            val remainingMs = session.plannedDurationMs - elapsedMs
            
            if (remainingMs > 0) {
                // Restart session with remaining time
                startSession(remainingMs, session.reminderIntervalMs)
            } else {
                // Session should have ended
                val completed = session.copy(
                    status = SessionStatus.COMPLETED,
                    endTime = Date()
                )
                sessionRepository.updateSession(completed)
            }
        }
    }
}
```

**Impact if not fixed:** Sessions lost if device reboots during active session.

---

## 🟡 LOW PRIORITY - Nice to Have

### 4. Add Missing Analytics Calls

Add these events:
- ImageLibraryViewModel: Track when images added/deleted
- TimerSetupViewModel: Track when duration/interval configured
- All screen navigation events

---

## Next Steps

1. **Fix critical issues** (1-2 above)
2. **Build the app** on Android Studio
3. **Deploy to test device** 
4. **Run through complete user flow**:
   - Open app
   - Go through onboarding
   - Add images
   - Grant permissions
   - Set timer (try 2 minutes for testing)
   - Start session
   - Lock screen
   - Verify nudge appears
   - Complete session
   - View summary
5. **Check Firebase console** for analytics events
6. **Fix any bugs discovered**

---

## Quick Test Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Install to connected device
./gradlew installDebug

# View logs
adb logcat -s Nudgr:* SessionEngineService:* ReminderActivity:*
```

---

**Estimated time to fix:** 30-60 minutes  
**Status:** Ready for development → testing → MVP release

