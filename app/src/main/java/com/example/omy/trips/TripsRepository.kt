package com.example.omy.trips

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.omy.data.OMYDatabase
import com.example.omy.data.Trip
import com.example.omy.data.TripDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TripsRepository(application: Application) {
    private var tripsDBDao: MutableList<TripDao>? = null

    init {
        val db: OMYDatabase? = OMYDatabase.getDatabase(application)
        if (db != null) { tripsDBDao = db.TripDao() }
    }

    companion object {
        private val scope = CoroutineScope(Dispatchers.IO)
        private class InsertAsyncTask(private val dao: TripDao?) : ViewModel() {
            suspend fun insertInBackground(vararg params: TripsDataset) {
                scope.launch {
                    for(param in params) {
                        val insertedId: String? = this@InsertAsyncTask.dao?.insert(param)?.toString()
                    }
                }
            }
        }
    }

    fun getTrips(): MutableList<Trip>? {
        return tripsDBDao?.getAll()
    }

    suspend fun createNewTrip() {
        // somehow create a new trip
        //InsertAsyncTask(tripsDBDao).insertInBackground()
    }
}