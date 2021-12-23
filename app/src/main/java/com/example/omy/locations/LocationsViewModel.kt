package com.example.omy.locations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.example.omy.data.*
import kotlinx.coroutines.runBlocking


class LocationsViewModel(application: Application): AndroidViewModel(application) {
    private var locationsRepository: LocationsRepository = LocationsRepository(application)
    private var locationsToDisplay: LiveData<List<Location?>>? = null
    private var photosLocationToDisplay: LiveData<List<LocationWithImages>>? = null

    init {
        this.locationsToDisplay = this.locationsRepository.getLocations()
        this.photosLocationToDisplay = this.locationsRepository.getLocationPhotos()
    }

    /**
     * Fetch specific locations
     *
     * @return List of locations data
     */
    fun getLocationsToDisplay(): LiveData<List<Location?>>? {
        if (this.locationsToDisplay == null) {
            this.locationsToDisplay = MutableLiveData()
        }
        return this.locationsToDisplay
    }

    /**
     * Launches a new location and creates it in the database
     *
     * @param location A location
     * @return void
     */
    fun createNewLocation(location: Location): Int =
        //viewModelScope.launch(Dispatchers.IO) { locationsRepository.createNewLocation(location) }
        runBlocking {
            var insertedID = locationsRepository.createNewLocation(location)
            insertedID
        }

    /**
     * Fetch specific photos for a specific location
     *
     * @return List of photos data
     */
    fun getLocationPhotosToDisplay(): LiveData<List<LocationWithImages>>? {
        if (this.photosLocationToDisplay == null) {
            this.photosLocationToDisplay = MutableLiveData()
        }
        return this.photosLocationToDisplay
    }

    /**
     * Launches a new photo for a specific location and creates it in the database
     *
     * @param location A photo's location
     * @return void
     */
    fun createNewPhotoLocation(location: ImageLocation) {
        //viewModelScope.launch(Dispatchers.IO) { locationsRepository.createNewLocation(location) }
        runBlocking {
            locationsRepository.createNewPhotoLocation(location)
        }
    }
}