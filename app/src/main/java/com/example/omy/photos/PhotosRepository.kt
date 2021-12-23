package com.example.omy.photos

import android.app.Application
import android.util.Log
import androidx.core.graphics.scaleMatrix
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
                var insertJob = 0
                scope.async { insertJob = this@InsertAsyncTask.dao?.insert(param)!!.toInt()}.await()
                return insertJob
            }
        }
    }

    /**
     * Function to get photos from database
     *
     * @return the list of photos data
     */
    fun getPhotos(): LiveData<List<Image>>? {
        return photosDBDao?.getAll()
    }

    /**
     * Function to create new photo and store it in database
     *
     * @return the new photo data
     */
    suspend fun createNewPhoto(photo : Image):Int {
        return  InsertAsyncTask(photosDBDao).insertInBackground(photo)
    }

}