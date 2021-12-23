package com.example.omy.photos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.omy.data.Image
import kotlinx.coroutines.runBlocking

/*
* PhotosViewModel.kt
* Mneimneh, Sekulski, Ooi 2021
* COM31007
*/
class PhotosViewModel(application: Application): AndroidViewModel(application) {
    private var photosRepository: PhotosRepository = PhotosRepository(application)
    private var photosToDisplay: LiveData<List<Image>>? = null

    init {
        this.photosToDisplay = this.photosRepository.getPhotos()
    }

    /**
     * Fetch specific photos
     *
     * @return List of photos data
     */
    fun getPhotosToDisplay(): LiveData<List<Image>>? {
        if (this.photosToDisplay == null) {
            this.photosToDisplay = MutableLiveData()
        }
        return this.photosToDisplay
    }

    /**
     * Launches a new photo and creates it in the database
     *
     * @param photo A photo
     * @return void
     */
    fun createNewPhoto(photo : Image):Int =
        runBlocking {
        var insertedID = photosRepository.createNewPhoto(photo)
        insertedID
        }
}