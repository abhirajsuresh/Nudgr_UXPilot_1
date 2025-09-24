package com.nudgr.data.local.dao

import androidx.room.*
import com.nudgr.data.local.entity.Session
import com.nudgr.data.local.entity.SessionStatus
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface SessionDao {
    @Query("SELECT * FROM sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<Session>>
    
    @Query("SELECT * FROM sessions WHERE id = :id")
    suspend fun getSessionById(id: String): Session?
    
    @Query("SELECT * FROM sessions WHERE status = :status ORDER BY startTime DESC LIMIT 1")
    suspend fun getLatestSessionByStatus(status: SessionStatus): Session?
    
    @Query("SELECT * FROM sessions WHERE status IN ('RUNNING', 'PAUSED') ORDER BY startTime DESC LIMIT 1")
    suspend fun getCurrentSession(): Session?
    
    @Query("SELECT * FROM sessions WHERE startTime >= :startDate AND startTime <= :endDate ORDER BY startTime DESC")
    fun getSessionsByDateRange(startDate: Date, endDate: Date): Flow<List<Session>>
    
    @Query("SELECT * FROM sessions WHERE DATE(startTime/1000, 'unixepoch') = DATE(:date/1000, 'unixepoch') ORDER BY startTime DESC")
    fun getSessionsByDate(date: Date): Flow<List<Session>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: Session)
    
    @Update
    suspend fun updateSession(session: Session)
    
    @Delete
    suspend fun deleteSession(session: Session)
    
    @Query("DELETE FROM sessions WHERE id = :id")
    suspend fun deleteSessionById(id: String)
}
