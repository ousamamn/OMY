package com.example.omy.trips

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.omy.data.Trip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TripsViewModel(application: Application): AndroidViewModel(application) {
    private var tripsRepository: TripsRepository = TripsRepository(application)
    private var tripsToDisplay: LiveData<Trip>? = null

    init {
        this.tripsToDisplay = this.tripsRepository.getTrips()
    }

    fun getTripsToDisplay(): LiveData<Trip>? {
        if (this.tripsToDisplay == null) {
            this.tripsToDisplay = MutableLiveData<Trip>()
        }
        return this.tripsToDisplay
    }

    fun createNewTrip() {

        }
    }