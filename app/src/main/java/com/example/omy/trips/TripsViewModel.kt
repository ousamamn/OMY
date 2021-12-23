package com.example.omy.trips

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.omy.data.Trip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class TripsViewModel(application: Application): AndroidViewModel(application) {
    private var tripsRepository: TripsRepository = TripsRepository(application)
    private var tripsToDisplay: LiveData<List<Trip?>>? = null
    private var lastTrip: LiveData<Trip?>? = null

    init {
        this.tripsToDisplay = tripsRepository.getTrips()
        this.lastTrip = tripsRepository.getLastTrip()
    }

    /**
     * Fetch specific trips
     *
     * @return List of trips
     */
    fun getTripsToDisplay(): LiveData<List<Trip?>>? {
        if (this.tripsToDisplay == null) {
            this.tripsToDisplay = MutableLiveData()
        }
        return this.tripsToDisplay
    }

    /**
     * Launches a new trip and creates it in the database
     *
     * @param trip A trip
     * @return void
     */
    fun createNewTrip(trip : Trip) {
        viewModelScope.launch(Dispatchers.IO) {tripsRepository.createNewTrip(trip) }
        }
    }