# 🚀 Nudgr – Android MVP Task Plan (Cursor-Ready)

**Project facts**
- Package: `com.nudgr`
- Min SDK: 26, Target: 34
- Layouts: `app/src/main/res/layout`
- Drawables/Fonts/Values: `app/src/main/res/{drawable,font,values}`
- Designs + HTML:  
  - `C:\Users\abhi9\Desktop\Projects\Nudgr with zux Pilot\designs\screens`  
  - `C:\Users\abhi9\Desktop\Projects\Nudgr with zux Pilot\designs\source`  
- Firebase configured (`google-services.json` in `app/`)
- SHA-1 / SHA-256 added in Firebase console  
- Firestore + Storage + Crashlytics + Auth + Analytics enabled

---

## ✅ Definition of Done (MVP)

- User completes onboarding checklist → starts a Deep Work session → receives overlay image nudges → ends session → sees summary.  
- Foreground service survives background & screen-off.  
- No crashes in a 25-minute run.  
- Crashlytics + Analytics events visible.  
- Google Sign-In optional, not required.

---

## 🧩 Architecture Overview

- **Single-Activity** app with `NavHostFragment`  
- **Fragments:** Welcome, Checklist, Permissions Hub, Image Library, Timer Setup, Dashboard, Summary  
- **Services:** `SessionService` (foreground) + `OverlayService`  
- **BroadcastReceiver:** `NudgeAlarmReceiver` (nudges or in-service ticker)  
- **Storage:** DataStore (+ optional Room) → Firestore sync later  
- **Images:** Coil  
- **DI:** Hilt  

---

## 🪜 STAGE A — Sanity & Cleanup

### 🧱 Task A1 — XML Audit & Auto-Fix
**Do**
- Scan `app/src/main/res/layout/*.xml` for:
  - Unclosed/self-closing tags  
  - Invalid enums (`gravity="space_between"`)  
  - Unsupported attrs (`orientation` on `RelativeLayout`)  
  - Bad `GridLayout` values (`rowCount="auto"`)  
- Verify IDs exist:  

  **Checklist:** `checklist_images`, `checklist_timers`, `checklist_permissions`, `btn_continue`, `progress_bar`, `progress_text`  
  **Dashboard:** `btn_start`, `btn_pause_resume`, `btn_end`, `tv_interval_value`, `tv_images_value`, `last_duration`, `last_unlocks`, `last_nudges`  
  **Image Library:** `btn_add_images`, `btn_select_all`, `btn_delete_selected`, `btn_add_first_images`, `image_grid`, `image_grid_container`, `empty_state`, `image_count_info`, `bottom_count`  
  **Overlay:** `reminder_image`, `btn_lock_phone`, `btn_snooze`, `btn_extend`  
  **Timer Setup:** `input_duration`, `input_interval`, `btn_start_session`  

- Replace missing fonts in XML with system fonts.  

**Output**
- Patch (diff) of XML changes + summary.  

**Accept**
- 0 aapt errors + Lint clean.

---

### 🧱 Task A2 — Remove Redundant Kotlin Version & Align Packages
**Do**
- Keep one Android app module (owns current XML).  
- `settings.gradle`: include only that module (e.g., `:app`).  
- Delete or archive duplicate Kotlin sources.  
- Ensure all Kotlin files use `package com.nudgr`.  
- Update imports accordingly.  

**Output**
- Diff removing redundant sources + final `settings.gradle`.  

**Accept**
- Gradle sync ok → one launchable config.

---

### 🧱 Task A3 — Gradle & Manifest Baseline
**Do**
- `app/build.gradle`:
  - `namespace "com.nudgr"`
  - `applicationId "com.nudgr"`
  - `compileSdk 34`, `minSdk 26`, `targetSdk 34`
  - `buildFeatures { viewBinding true }`
  - Add deps: Material, ConstraintLayout, Navigation, Lifecycle, Coroutines, DataStore, Coil, Hilt (+ kapt), Firebase BOM + Auth/Firestore/Storage/Analytics/Crashlytics  
- Manifest:
  - Permissions: `SYSTEM_ALERT_WINDOW`, `FOREGROUND_SERVICE`, `WAKE_LOCK`, `POST_NOTIFICATIONS`, `SCHEDULE_EXACT_ALARM` (optional)  
  - Services: `SessionService`, `OverlayService`, `NudgeAlarmReceiver`, `BootRestoreReceiver`  

**Output**
- Diff of Gradle + Manifest.  

**Accept**
- Project syncs and runs (blank screen ok).

---

## ⚙️ STAGE B — Core Runtime Pipeline

### 🔧 Task B1 — SessionService (Foreground)
**Do**
- Foreground service with 250 ms tick loop.  
- Extras: `durationMs`, `intervalMs`.  
- Internal or broadcast “next nudge” scheduler.  
- Foreground notification helper.  

**Output**
- Kotlin file(s) + notification builder.  

**Accept**
- Starts/stops cleanly; ticks logged in Logcat.

---

### 🔧 Task B2 — NudgeAlarmReceiver + OverlayService
**Do**
- `NudgeAlarmReceiver` → starts `OverlayService`.  
- `OverlayService` inflates overlay XML; wire: 
  - `btn_snooze`, `btn_extend` (toasts OK)
  - `btn_lock_phone` (placeholder)
  - Dismiss logic.  
- Use `TYPE_APPLICATION_OVERLAY`; handle permission check.  

**Output**
- Kotlin receiver + service + permission util.  

**Accept**
- Manual test: overlay appears → dismiss works.

---

## 🗂️ STAGE C — Settings & Navigation

### 🧩 Task C1 — DataStore (Preferences)
**Do**
- Keys: `duration_ms`, `interval_ms`, `has_images`, `has_permissions`.  
- `SettingsViewModel` exposes flows + setters.  
- Provide Hilt module.  

**Output**
- Kotlin DataStore + ViewModel.  

**Accept**
- Values persist after restart.

---

### 🧩 Task C2 — NavGraph + ViewBinding Wiring
**Do**
- Create `nav_graph.xml` flow: Welcome → Checklist → (Permissions | Image Library | Timer Setup) → Dashboard → Summary.  
- Wire fragments to existing XML via ViewBinding.  

**Output**
- NavGraph XML + fragment stubs.  

**Accept**
- All screens navigate without crashes.

---

## 🖼️ STAGE D — Image Library & Reports

### 🖼️ Task D1 — Image Library (MVP)
**Do**
- Use existing XML (grid + item card).  
- Implement: add images (picker), select all/delete, empty vs grid visibility, count updates.  
- Bind `image_thumb`, `image_title`, `image_date`, `selection_overlay`.  

**Output**
- Fragment + local URI repo.  

**Accept**
- Add 3 images → grid renders → select/delete works.

---

### 📊 Task D2 — Basic Reports (Local)
**Do**
- Local `SessionLog` store (file or Room).  
- Compute daily/weekly totals (minutes + nudges).  
- Simple list UI (chart later).  

**Output**
- Fragment + aggregator utility.  

**Accept**
- Summary shows expected totals.

---

## 🧠 STAGE E — Stability & Telemetry

### 📈 Task E1 — Analytics & Crashlytics
**Do**
- Events: `onboarding_step_complete`, `session_start`, `nudge_shown`, `session_complete`.  
- Initialize Crashlytics (no forced crash).  

**Output**
- Analytics helper + event calls.  

**Accept**
- Events appear in Firebase (console delay ok).

---

### 🧪 Task E2 — QA Smoke Tests
**Do**
- Run on API 26, 30, 34 emulators.  
- Verify: overlay permission, service persistence, overlay visibility, no crashes in 5-min session.  

**Output**
- Bullet test log.  

**Accept**
- All pass; 0 crashes.

---

## ☁️ (OPTIONAL FINAL) STAGE F — Cloud Sync

### ☁️ Task F1 — Firestore Sync
**Do**
- Sync `UserSettings` + `SessionLog` to Firestore when signed in.  
- Maintain offline functionality.  

**Output**
- Firestore DAO + auth gate.  

**Accept**
- Sign-in → data appears under `/users/{uid}/…`.

---

## 🧰 Cursor One-Shot Prompt

