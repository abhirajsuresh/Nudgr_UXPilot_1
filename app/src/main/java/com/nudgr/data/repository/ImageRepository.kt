package com.nudgr.data.repository

import com.nudgr.data.local.dao.ImageDao
import com.nudgr.data.local.entity.Image
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepository @Inject constructor(
    private val imageDao: ImageDao
) {
    fun getAllActiveImages(): Flow<List<Image>> = imageDao.getAllActiveImages()
    
    fun getAllImages(): Flow<List<Image>> = imageDao.getAllImages()
    
    suspend fun getImageById(id: String): Image? = imageDao.getImageById(id)
    
    suspend fun getActiveImageCount(): Int = imageDao.getActiveImageCount()
    
    suspend fun getRandomActiveImage(): Image? = imageDao.getRandomActiveImage()
    
    suspend fun insertImage(image: Image) = imageDao.insertImage(image)
    
    suspend fun insertImages(images: List<Image>) = imageDao.insertImages(images)
    
    suspend fun updateImage(image: Image) = imageDao.updateImage(image)
    
    suspend fun deleteImage(image: Image) = imageDao.deleteImage(image)
    
    suspend fun deleteImageById(id: String) = imageDao.deleteImageById(id)
    
    suspend fun deleteImagesByIds(ids: List<String>) = imageDao.deleteImagesByIds(ids)
    
    suspend fun getImageByHash(hash: String): Image? = imageDao.getImageByHash(hash)
}
