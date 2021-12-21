package com.example.omy.trips

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.omy.data.Trip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.example.omy.data.Location


class TripsViewModel(application: Application): AndroidViewModel(application) {
    private var tripsRepository: TripsRepository = TripsRepository(application)
    private var tripsToDisplay: LiveData<List<Trip?>>? = null
    private var lastTrip: LiveData<Trip?>? = null

    init {
        this.tripsToDisplay = tripsRepository.getTrips()
        this.lastTrip = tripsRepository.getLastTrip()
    }

    fun getTripsToDisplay(): LiveData<List<Trip?>>? {
        if (this.tripsToDisplay == null) {
            this.tripsToDisplay = MutableLiveData()
        }
        return this.tripsToDisplay
    }

    fun createNewTrip(trip : Trip) {
        viewModelScope.launch(Dispatchers.IO) {tripsRepository.createNewTrip(trip) }
        }
    }