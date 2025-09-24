package com.nudgr.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.nudgr.data.local.dao.ImageDao
import com.nudgr.data.local.dao.SessionDao
import com.nudgr.data.local.dao.SessionEventDao
import com.nudgr.data.local.entity.Image
import com.nudgr.data.local.entity.Session
import com.nudgr.data.local.entity.SessionEvent

@Database(
    entities = [Image::class, Session::class, SessionEvent::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class NudgrDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
    abstract fun sessionDao(): SessionDao
    abstract fun sessionEventDao(): SessionEventDao
    
    companion object {
        @Volatile
        private var INSTANCE: NudgrDatabase? = null
        
        fun getDatabase(context: Context): NudgrDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NudgrDatabase::class.java,
                    "nudgr_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
