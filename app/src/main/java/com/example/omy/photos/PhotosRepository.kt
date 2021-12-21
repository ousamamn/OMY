package com.example.omy.photos

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.omy.MainRepository
import com.example.omy.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class PhotosRepository(application: Application) {
    private var photosDBDao: ImageDao? = null
    private val databaseObj:OMYDatabase? = MainRepository(application).databaseObj
    init {
        if (databaseObj != null) {
            photosDBDao = databaseObj.ImageDao()
        }
    }



    companion object {
        private val scope = CoroutineScope(Dispatchers.IO)
        private class InsertAsyncTask(private val dao: ImageDao?) : ViewModel() {
            suspend fun insertInBackground(param: Image):Int {
                var insertedId = 0
                scope.launch {
                    insertedId = async{this@InsertAsyncTask.dao?.insert(param)}.await()?.toInt()!!

                }
                return insertedId
            }
        }
    }

    fun getPhotos(): LiveData<List<Image>>? {
        return photosDBDao?.getAll()
    }

    suspend fun createNewPhoto(photo : Image):Int {
        // somehow create a new trip
       return InsertAsyncTask(photosDBDao).insertInBackground(photo)
    }
}