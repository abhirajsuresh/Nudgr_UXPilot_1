package com.nudgr.data.repository

import com.nudgr.data.local.dao.SessionDao
import com.nudgr.data.local.dao.SessionEventDao
import com.nudgr.data.local.entity.Session
import com.nudgr.data.local.entity.SessionEvent
import com.nudgr.data.local.entity.SessionEventType
import com.nudgr.data.local.entity.SessionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionRepository @Inject constructor(
    private val sessionDao: SessionDao,
    private val sessionEventDao: SessionEventDao
) {
    fun getAllSessions(): Flow<List<Session>> = sessionDao.getAllSessions()
    
    suspend fun getSessionById(id: String): Session? = sessionDao.getSessionById(id)
    
    suspend fun getCurrentSession(): Session? = sessionDao.getCurrentSession()
    
    suspend fun getLatestSession(): Session? = sessionDao.getCurrentSession()
    
    fun getSessionsByDateRange(startDate: Date, endDate: Date): Flow<List<Session>> = 
        sessionDao.getSessionsByDateRange(startDate, endDate)
    
    fun getSessionsByDate(date: Date): Flow<List<Session>> = 
        sessionDao.getSessionsByDate(date)
    
    suspend fun insertSession(session: Session) = sessionDao.insertSession(session)
    
    suspend fun updateSession(session: Session) = sessionDao.updateSession(session)
    
    suspend fun deleteSession(session: Session) = sessionDao.deleteSession(session)
    
    suspend fun deleteSessionById(id: String) = sessionDao.deleteSessionById(id)
    
    // Session Events
    fun getEventsBySessionId(sessionId: String): Flow<List<SessionEvent>> = 
        sessionEventDao.getEventsBySessionId(sessionId)
    
    fun getEventsBySessionAndType(sessionId: String, eventType: SessionEventType): Flow<List<SessionEvent>> = 
        sessionEventDao.getEventsBySessionAndType(sessionId, eventType)
    
    suspend fun getLatestEventBySessionAndType(sessionId: String, eventType: SessionEventType): SessionEvent? = 
        sessionEventDao.getLatestEventBySessionAndType(sessionId, eventType)
    
    suspend fun getEventCountBySessionAndType(sessionId: String, eventType: SessionEventType): Int = 
        sessionEventDao.getEventCountBySessionAndType(sessionId, eventType)
    
    suspend fun insertEvent(event: SessionEvent) = sessionEventDao.insertEvent(event)
    
    suspend fun insertEvents(events: List<SessionEvent>) = sessionEventDao.insertEvents(events)
    
    suspend fun deleteEvent(event: SessionEvent) = sessionEventDao.deleteEvent(event)
    
    suspend fun deleteEventsBySessionId(sessionId: String) = sessionEventDao.deleteEventsBySessionId(sessionId)
    
    // Statistics and Aggregation
    suspend fun getSessionsByDateOnly(date: Date): List<Session> {
        val calendar = Calendar.getInstance().apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfDay = calendar.time
        
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val endOfDay = calendar.time
        
        return getSessionsByDateRange(startOfDay, endOfDay).firstOrNull() ?: emptyList()
    }
    
    suspend fun getDailyStats(date: Date = Date()): SessionStats {
        val sessions = getSessionsByDateOnly(date)
        val completedSessions = sessions.filter { it.status == SessionStatus.COMPLETED }
        
        return SessionStats(
            totalDurationMs = completedSessions.sumOf { it.actualDurationMs },
            totalSessions = completedSessions.size,
            totalReminders = completedSessions.sumOf { it.totalReminders },
            totalUnlocks = completedSessions.sumOf { it.totalUnlocks },
            totalSnoozes = completedSessions.sumOf { it.totalSnoozes }
        )
    }
    
    suspend fun getWeeklyStats(startDate: Date = getStartOfWeek()): SessionStats {
        val calendar = Calendar.getInstance().apply {
            time = startDate
        }
        val endDate = calendar.apply { add(Calendar.DAY_OF_MONTH, 7) }.time
        
        val sessions = getSessionsByDateRange(startDate, endDate).firstOrNull() ?: emptyList()
        val completedSessions = sessions.filter { it.status == SessionStatus.COMPLETED }
        
        return SessionStats(
            totalDurationMs = completedSessions.sumOf { it.actualDurationMs },
            totalSessions = completedSessions.size,
            totalReminders = completedSessions.sumOf { it.totalReminders },
            totalUnlocks = completedSessions.sumOf { it.totalUnlocks },
            totalSnoozes = completedSessions.sumOf { it.totalSnoozes }
        )
    }
    
    suspend fun getMonthlyStats(date: Date = Date()): SessionStats {
        val calendar = Calendar.getInstance().apply {
            time = date
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfMonth = calendar.time
        
        calendar.add(Calendar.MONTH, 1)
        val endOfMonth = calendar.time
        
        val sessions = getSessionsByDateRange(startOfMonth, endOfMonth).firstOrNull() ?: emptyList()
        val completedSessions = sessions.filter { it.status == SessionStatus.COMPLETED }
        
        return SessionStats(
            totalDurationMs = completedSessions.sumOf { it.actualDurationMs },
            totalSessions = completedSessions.size,
            totalReminders = completedSessions.sumOf { it.totalReminders },
            totalUnlocks = completedSessions.sumOf { it.totalUnlocks },
            totalSnoozes = completedSessions.sumOf { it.totalSnoozes }
        )
    }
    
    private fun getStartOfWeek(): Date {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.time
    }
    
    data class SessionStats(
        val totalDurationMs: Long = 0L,
        val totalSessions: Int = 0,
        val totalReminders: Int = 0,
        val totalUnlocks: Int = 0,
        val totalSnoozes: Int = 0
    ) {
        val totalDurationMinutes: Int get() = (totalDurationMs / (60 * 1000)).toInt()
        val totalDurationHours: Float get() = totalDurationMs / (60 * 60 * 1000f)
        val averageSessionMinutes: Int get() = if (totalSessions > 0) totalDurationMinutes / totalSessions else 0
    }
}
