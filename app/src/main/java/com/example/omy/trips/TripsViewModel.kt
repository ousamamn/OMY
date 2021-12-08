package com.example.omy.trips

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.omy.data.Trip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TripsViewModel(application: Application): AndroidViewModel(application) {
    private var tripsReposiotry: TripsRepository = TripsRepository(application)
    private var tripsToDisplay: MutableList<Trip>? = null

    init {
        this.tripsToDisplay = this.tripsReposiotry.getTrips()
    }

    fun getTripsToDisplay(): MutableList<Trip>? {
        if (this.tripsToDisplay == null) {
            this.tripsToDisplay = MutableList()
        }
        return this.tripsToDisplay
    }

    fun createNewTrip() {
        GlobalScope.launch(Dispatchers.IO) {
            tripsReposiotry.createNewTrip()
        }
    }
}