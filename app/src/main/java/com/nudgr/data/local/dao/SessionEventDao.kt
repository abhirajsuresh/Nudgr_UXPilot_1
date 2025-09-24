package com.nudgr.data.local.dao

import androidx.room.*
import com.nudgr.data.local.entity.SessionEvent
import com.nudgr.data.local.entity.SessionEventType
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface SessionEventDao {
    @Query("SELECT * FROM session_events WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    fun getEventsBySessionId(sessionId: String): Flow<List<SessionEvent>>
    
    @Query("SELECT * FROM session_events WHERE sessionId = :sessionId AND eventType = :eventType ORDER BY timestamp ASC")
    fun getEventsBySessionAndType(sessionId: String, eventType: SessionEventType): Flow<List<SessionEvent>>
    
    @Query("SELECT * FROM session_events WHERE sessionId = :sessionId AND eventType = :eventType ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestEventBySessionAndType(sessionId: String, eventType: SessionEventType): SessionEvent?
    
    @Query("SELECT COUNT(*) FROM session_events WHERE sessionId = :sessionId AND eventType = :eventType")
    suspend fun getEventCountBySessionAndType(sessionId: String, eventType: SessionEventType): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: SessionEvent)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<SessionEvent>)
    
    @Delete
    suspend fun deleteEvent(event: SessionEvent)
    
    @Query("DELETE FROM session_events WHERE sessionId = :sessionId")
    suspend fun deleteEventsBySessionId(sessionId: String)
}
