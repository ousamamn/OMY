package com.example.omy.locations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.omy.data.Trip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.example.omy.data.Image
import com.example.omy.data.Location


class LocationsViewModel(application: Application): AndroidViewModel(application) {
    private var locationsRepository: LocationsRepository = LocationsRepository(application)
    private var locationsToDisplay: LiveData<List<Location?>>? = null

    init {
        this.locationsToDisplay = this.locationsRepository.getLocations()
    }

    fun getLocationsToDisplay(): LiveData<List<Location?>>? {
        if (this.locationsToDisplay == null) {
            this.locationsToDisplay = MutableLiveData()
        }
        return this.locationsToDisplay
    }

    fun createNewLocation(location: Location) {
        viewModelScope.launch(Dispatchers.IO) { locationsRepository.createNewLocation(location) }
        }
    }