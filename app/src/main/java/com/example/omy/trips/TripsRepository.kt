package com.example.omy.trips

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.omy.MainRepository
import com.example.omy.data.Location
import com.example.omy.data.OMYDatabase
import com.example.omy.data.Trip
import com.example.omy.data.TripDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            suspend fun insertInBackground(vararg params: Trip){
                scope.launch {
                    for(param in params) {
                        this@InsertAsyncTask.dao?.insert(param)
                    }
                }
            }
        }
    }

    /**
     * Function to get trips from database
     *
     * @return the list of trips data
     */
    fun getTrips(): LiveData<List<Trip?>>? {
        return tripsDBDao?.getAll()
    }

    /**
     * Function to get the last trip from database
     *
     * @return the last trip data
     */
    fun getLastTrip(): LiveData<Trip?>? {
        return tripsDBDao?.getLastTrip()
    }

    /**
     * Function to create new trip and save database
     *
     * @return new trip data in database
     */
    suspend fun createNewTrip(trip : Trip){
        // somehow create a new trip
        InsertAsyncTask(tripsDBDao).insertInBackground(trip)
    }

}