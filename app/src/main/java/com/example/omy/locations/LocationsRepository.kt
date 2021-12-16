package com.example.omy.locations

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.omy.MainRepository
import com.example.omy.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationsRepository(application: Application) {
    private var locationsDBDao: LocationDao? = null

    private val databaseObj:OMYDatabase? = MainRepository(application).databaseObj
    init {
        if (databaseObj != null) {
            locationsDBDao = databaseObj.LocationDao()
        }
    }

    companion object {
        private val scope = CoroutineScope(Dispatchers.IO)
        private class InsertAsyncTask(private val dao: LocationDao?) : ViewModel() {
            suspend fun insertInBackground(vararg params: Location) {
                scope.launch {
                    for(param in params) {
                        val insertedId = this@InsertAsyncTask.dao?.insert(param)
                    }
                }
            }
        }
    }

    fun getLocations(): LiveData<List<Location>>? {
        return locationsDBDao?.getAll()
    }

    suspend fun createNewLocation(location : Location) {
        // somehow create a new trip
        InsertAsyncTask(locationsDBDao).insertInBackground(location)
    }
}