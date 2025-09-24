package com.nudgr.data.repository

import com.nudgr.data.local.dao.SessionDao
import com.nudgr.data.local.dao.SessionEventDao
import com.nudgr.data.local.entity.Session
import com.nudgr.data.local.entity.SessionEvent
import com.nudgr.data.local.entity.SessionEventType
import com.nudgr.data.local.entity.SessionStatus
import kotlinx.coroutines.flow.Flow
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
}
