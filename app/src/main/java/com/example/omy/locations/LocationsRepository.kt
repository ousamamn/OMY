package com.example.omy.locations

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.omy.MainRepository
import com.example.omy.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LocationsRepository(application: Application) {
    private var locationsDBDao: LocationDao? = null
    private var photosLocationDBDao: ImageLocationDao? = null

    private val databaseObj:OMYDatabase? = MainRepository(application).databaseObj
    init {
        if (databaseObj != null) {
            locationsDBDao = databaseObj.LocationDao()
            photosLocationDBDao = databaseObj.ImageLocationDao()
        }
    }

    companion object {
        private val scope = CoroutineScope(Dispatchers.IO)
        private class InsertAsyncTask(private val dao: LocationDao?) : ViewModel() {
            suspend fun insertInBackground(param: Location):Int {
                var insertJob=0
                scope.async { insertJob = this@InsertAsyncTask.dao?.insert(param)!!.toInt()}.await()
                return insertJob
            }
        }


        private class InsertAsyncTask2(private val dao: ImageLocationDao?) : ViewModel() {
            suspend fun insertInBackground2(param: ImageLocation) {
                var insertedId = 0
                scope.launch {
                    async{this@InsertAsyncTask2.dao?.insert(param)}

                }
            }
        }
    }

    /**
     * Get all the existing locations
     *
     * @return all locations
     */
    fun getLocations(): LiveData<List<Location?>>? {
        return locationsDBDao?.getAll()
    }

    /**
     * Creates a new location, saving it to the database
     *
     * @param location A location to be saved
     * @return void
     */
    suspend fun createNewLocation(location : Location):Int {
        // somehow create a new trip
        return InsertAsyncTask(locationsDBDao).insertInBackground(location)
    }

    /**
     * Get all the existing locations' photos
     *
     * @return all locations' photos
     */
    fun getLocationPhotos():LiveData<List<LocationWithImages>>? {
        return photosLocationDBDao?.getLocationWithImages()
    }

    /**
     * Creates a new photo for a specific location, saving it to the database
     *
     * @param photoLocation A photo for a specific location to be saved
     * @return void
     */
    suspend fun createNewPhotoLocation(photoLocation : ImageLocation) {
        InsertAsyncTask2(photosLocationDBDao).insertInBackground2(photoLocation)
    }
}