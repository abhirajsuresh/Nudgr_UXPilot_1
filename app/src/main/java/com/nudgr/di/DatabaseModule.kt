package com.nudgr.di

import android.content.Context
import androidx.room.Room
import com.nudgr.data.local.dao.ImageDao
import com.nudgr.data.local.dao.SessionDao
import com.nudgr.data.local.dao.SessionEventDao
import com.nudgr.data.local.database.NudgrDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideNudgrDatabase(@ApplicationContext context: Context): NudgrDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            NudgrDatabase::class.java,
            "nudgr_database"
        ).build()
    }
    
    @Provides
    fun provideImageDao(database: NudgrDatabase): ImageDao = database.imageDao()
    
    @Provides
    fun provideSessionDao(database: NudgrDatabase): SessionDao = database.sessionDao()
    
    @Provides
    fun provideSessionEventDao(database: NudgrDatabase): SessionEventDao = database.sessionEventDao()
}
