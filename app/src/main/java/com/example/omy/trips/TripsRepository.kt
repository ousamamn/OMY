package com.example.omy.trips

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.omy.data.OMYDatabase
import com.example.omy.data.Trip
import com.example.omy.data.TripDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TripsRepository(application: Application) {
    private var tripsDBDao: TripDao? = null

    init {
        val db: OMYDatabase? = OMYDatabase.getDatabase(application)
        if (db != null) { tripsDBDao = db.TripDao() }
    }

    companion object {
        private val scope = CoroutineScope(Dispatchers.IO)
        private class InsertAsyncTask(private val dao: TripDao?) : ViewModel() {
            suspend fun insertInBackground(vararg params: Trip) {
                scope.launch {
                    for(param in params) {
                        val insertedId = this@InsertAsyncTask.dao?.insert(param)
                    }
                }
            }
        }
    }

    fun getTrips(): LiveData<Trip>? {
        return tripsDBDao?.getAll()
    }

    suspend fun createNewTrip(trip : Trip) {
        // somehow create a new trip
        InsertAsyncTask(tripsDBDao).insertInBackground(trip)
    }
}