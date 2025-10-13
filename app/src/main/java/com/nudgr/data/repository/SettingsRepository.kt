package com.nudgr.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferencesKeys {
        val DURATION_MS = longPreferencesKey("duration_ms")
        val INTERVAL_MS = longPreferencesKey("interval_ms")
    }

    val durationMs: Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.DURATION_MS] ?: (4 * 60 * 60 * 1000L) // Default 4 hours
        }

    val intervalMs: Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.INTERVAL_MS] ?: (2 * 60 * 1000L) // Default 2 minutes
        }

    suspend fun saveTimers(durationMs: Long, intervalMs: Long) {
        context.dataStore.edit { settings ->
            settings[PreferencesKeys.DURATION_MS] = durationMs
            settings[PreferencesKeys.INTERVAL_MS] = intervalMs
        }
    }
}
