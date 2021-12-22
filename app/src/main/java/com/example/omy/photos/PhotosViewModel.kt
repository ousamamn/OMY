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


class PhotosViewModel(application: Application): AndroidViewModel(application) {
    private var photosRepository: PhotosRepository = PhotosRepository(application)
    private var photosToDisplay: LiveData<List<Image>>? = null

    init {
        this.photosToDisplay = this.photosRepository.getPhotos()
    }

    fun getPhotosToDisplay(): LiveData<List<Image>>? {
        if (this.photosToDisplay == null) {
            this.photosToDisplay = MutableLiveData()
        }
        return this.photosToDisplay
    }

    fun createNewPhoto(photo : Image):Int {
        var insertedID =0
        viewModelScope.launch(Dispatchers.IO) { insertedID = photosRepository.createNewPhoto(photo) }
        return insertedID
        }


    }