package com.example.omy.photos

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.omy.MainRepository
import com.example.omy.data.*
import kotlinx.coroutines.*

class PhotosRepository(application: Application) {
    private var photosDBDao: ImageDao? = null
    private var photosLocationDBDao: ImageLocationDao? = null
    private val databaseObj:OMYDatabase? = MainRepository(application).databaseObj

    init {
        if (databaseObj != null) {
            photosDBDao = databaseObj.ImageDao()

            photosLocationDBDao = databaseObj.ImageLocationDao()
        }
    }

    companion object {
        private val scope = CoroutineScope(Dispatchers.IO)

        private class InsertAsyncTask(private val dao: ImageDao?) : ViewModel() {
            suspend fun insertInBackground(param: Image): Int {
                var insertJob=0
                scope.async { insertJob = this@InsertAsyncTask.dao?.insert(param)!!.toInt()}.await()
                return insertJob
                }
        }
    }

    /**
     * Get all the existing photos
     *
     * @return all photos
     */
    fun getPhotos(): LiveData<List<Image>>? {
        return photosDBDao?.getAll()
    }

    /**
     * Creates a new photo, saving it to the database
     *
     * @param photo A photo to be saved
     * @return void
     */
    suspend fun createNewPhoto(photo : Image):Int {
        return  InsertAsyncTask(photosDBDao).insertInBackground(photo)
        }

}