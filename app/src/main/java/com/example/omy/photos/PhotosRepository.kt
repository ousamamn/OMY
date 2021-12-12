package com.example.omy.photos

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.omy.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhotosRepository(application: Application) {
    private var photosDBDao: ImageDao? = null

    init {
        val db: OMYDatabase? = OMYDatabase.getDatabase(application)
        if (db != null) { photosDBDao = db.ImageDao() }
    }

    companion object {
        private val scope = CoroutineScope(Dispatchers.IO)
        private class InsertAsyncTask(private val dao: ImageDao?) : ViewModel() {
            suspend fun insertInBackground(vararg params: Image) {
                scope.launch {
                    for(param in params) {
                        val insertedId = this@InsertAsyncTask.dao?.insert(param)
                    }
                }
            }
        }
    }

    fun getPhotos(): LiveData<List<Image>>? {
        return photosDBDao?.getAll()
    }

    suspend fun createNewPhoto(photo : Image) {
        // somehow create a new trip
        InsertAsyncTask(photosDBDao).insertInBackground(photo)
    }
}