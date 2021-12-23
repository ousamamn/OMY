package com.example.omy.trips

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.omy.MainRepository
import com.example.omy.data.OMYDatabase
import com.example.omy.data.Trip
import com.example.omy.data.TripDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
* TripsRepository.kt
* Mneimneh, Sekulski, Ooi 2021
* COM31007
*/
class TripsRepository(application: Application) {
    private var tripsDBDao: TripDao? = null
    var id:Int? =0
    private val databaseObj:OMYDatabase? = MainRepository(application).databaseObj

    init {
        if (databaseObj != null) {
            tripsDBDao = databaseObj.TripDao()
        }
    }

    companion object {
        private val scope = CoroutineScope(Dispatchers.IO)
        private class InsertAsyncTask(private val dao: TripDao?) : ViewModel() {
            fun insertInBackground(vararg params: Trip){
                scope.launch {
                    for(param in params) {
                        this@InsertAsyncTask.dao?.insert(param)
                    }
                }
            }
        }
    }

    /**
     * Get all the existing trips
     *
     * @return all trips
     */
    fun getTrips(): LiveData<List<Trip?>>? {
        return tripsDBDao?.getAll()
    }

    /**
     * Get the newest existing trip
     *
     * @return latest trip
     */
    fun getLastTrip(): LiveData<Trip?>? {
        return tripsDBDao?.getLastTrip()
    }

    /**
     * Creates a new trip, saving it to the database
     *
     * @param trip A trip to be saved
     * @return void
     */
    suspend fun createNewTrip(trip : Trip){
        // somehow create a new trip
        InsertAsyncTask(tripsDBDao).insertInBackground(trip)
    }

}