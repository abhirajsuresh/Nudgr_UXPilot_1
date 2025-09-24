package com.nudgr.data.local.dao

import androidx.room.*
import com.nudgr.data.local.entity.Image
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    @Query("SELECT * FROM images WHERE isActive = 1 ORDER BY addedAt DESC")
    fun getAllActiveImages(): Flow<List<Image>>
    
    @Query("SELECT * FROM images ORDER BY addedAt DESC")
    fun getAllImages(): Flow<List<Image>>
    
    @Query("SELECT * FROM images WHERE id = :id")
    suspend fun getImageById(id: String): Image?
    
    @Query("SELECT COUNT(*) FROM images WHERE isActive = 1")
    suspend fun getActiveImageCount(): Int
    
    @Query("SELECT * FROM images WHERE isActive = 1 ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomActiveImage(): Image?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: Image)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(images: List<Image>)
    
    @Update
    suspend fun updateImage(image: Image)
    
    @Delete
    suspend fun deleteImage(image: Image)
    
    @Query("DELETE FROM images WHERE id = :id")
    suspend fun deleteImageById(id: String)
    
    @Query("DELETE FROM images WHERE id IN (:ids)")
    suspend fun deleteImagesByIds(ids: List<String>)
    
    @Query("SELECT * FROM images WHERE contentHash = :hash")
    suspend fun getImageByHash(hash: String): Image?
}
