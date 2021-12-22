package com.example.omy.photos

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.omy.data.Trip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.example.omy.data.Image
import com.example.omy.data.ImageLocation
import com.example.omy.data.LocationWithImages
import kotlinx.coroutines.runBlocking


class PhotosViewModel(application: Application): AndroidViewModel(application) {
    private var photosRepository: PhotosRepository = PhotosRepository(application)
    private var photosToDisplay: LiveData<List<Image>>? = null

    init {
        this.photosToDisplay = this.photosRepository.getPhotos()
    }

    /**
     * Function to get photo
     *
     * @return the list of photos data
     */
    fun getPhotosToDisplay(): LiveData<List<Image>>? {
        if (this.photosToDisplay == null) {
            this.photosToDisplay = MutableLiveData()
        }
        return this.photosToDisplay
    }

    /**
     * Function to create new photo
     */
    fun createNewPhoto(photo : Image):Int =
        runBlocking {
        var insertedID = photosRepository.createNewPhoto(photo)
        insertedID
        }


}