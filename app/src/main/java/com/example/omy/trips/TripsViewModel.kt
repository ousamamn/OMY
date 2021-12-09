package com.example.omy.trips

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.omy.data.Trip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope


class TripsViewModel(application: Application): AndroidViewModel(application) {
    private var tripsRepository: TripsRepository = TripsRepository(application)
    private var tripsToDisplay: LiveData<List<Trip>>? = null

    init {
        this.tripsToDisplay = this.tripsRepository.getTrips()
    }

    fun getTripsToDisplay(): LiveData<List<Trip>>? {
        if (this.tripsToDisplay == null) {
            this.tripsToDisplay = MutableLiveData()
        }
        return this.tripsToDisplay
    }

    fun createNewTrip(trip : Trip) {
        viewModelScope.launch(Dispatchers.IO) { tripsRepository.createNewTrip(trip) }
        }
    }