package com.example.omy.maps

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.omy.R
import com.example.omy.data.Image
import com.example.omy.data.Location
import com.example.omy.locations.LocationsViewModel
import com.example.omy.photos.PhotosViewModel
import kotlinx.coroutines.runBlocking
import pl.aprilapps.easyphotopicker.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

/*
* MapAddActivity.kt
* This file provides the users
  a title field input, a description field input,
  a latitude and longitude field input
  and a photo adding function to create a new location.
* Mneimneh, Sekulski, Ooi 2021
* COM31007
*/
class MapAddActivity : AppCompatActivity() {
    private lateinit var displayTitle: TextView
    private lateinit var latEditText: EditText
    private lateinit var longEditText: EditText
    private lateinit var cancelButton: Button
    private lateinit var saveButton: Button
    private lateinit var addPhotoButton: View
    private lateinit var titleNameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var easyImage: EasyImage
    private var locationsViewModel: LocationsViewModel? = null
    private var photosViewModel: PhotosViewModel? = null
    val imageList: MutableList<Image> = ArrayList<Image>()

    private var tripLatitude: Double = 0.0
    private var tripLongitude: Double = 0.0
    //private var locationDate: String = ""

    private val intentPhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_created_location)

        locationsViewModel = ViewModelProvider(this)[LocationsViewModel::class.java]
        photosViewModel = ViewModelProvider(this)[PhotosViewModel::class.java]

        // Receive information from MapCreatedActivity
        val b: Bundle? = intent.extras
        if (b != null) {
            displayTitle = findViewById(R.id.trip_title)
            displayTitle.text = b.getString("trip_title")
            tripLatitude = b.getDouble("trip_latitude")
            tripLongitude = b.getDouble("trip_longitude")
            //locationDate = b.getString("location_date").toString()
        }

        // Set up easyImage for taking photos
        easyImage= EasyImage.Builder(this)
            .setChooserType(ChooserType.CAMERA_AND_GALLERY)
            .setCopyImagesToPublicGalleryFolder(false)
            .setFolderName("EasyImage sample")
            .build()

        titleNameEditText = findViewById(R.id.location_title)
        descriptionEditText = findViewById(R.id.location_description)
        latEditText = findViewById(R.id.location_latitude)
        latEditText.hint = "%.2f".format(tripLatitude)
        longEditText = findViewById(R.id.location_longitude)
        longEditText.hint = "%.2f".format(tripLongitude)
        cancelButton = findViewById(R.id.location_cancel_button)
        saveButton = findViewById(R.id.location_add_button)
        addPhotoButton = findViewById(R.id.location_add_photo)

        // Cancel the add new location and return to the previous page button
        cancelButton.setOnClickListener {
            onBackPressed()
            finish()
        }

        // Save the new location to the database button
        saveButton.setOnClickListener {
            if (TextUtils.isEmpty(titleNameEditText.text.toString())) {
                Toast
                    .makeText(applicationContext, "Please insert the title", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(descriptionEditText.text.toString())) {
                Toast.makeText(applicationContext, "Please insert the description", Toast.LENGTH_SHORT).show();
            } else {
                saveLocationToDB()
                onBackPressed()
                finish()
            }
        }

        // Add photo button
        addPhotoButton.setOnClickListener {
            easyImage.openCameraForImage(this)
        }
    }

    /**
     * Fetch current activity content
     *
     * @param requestCode integer
     * @param resultCode integer
     * @param data the parameters from previous activity or fragment
     * @return void
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        easyImage.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {
            /**
             * Fetch the list of photos
             *
             * @param imageFiles Array list of photos
             * @param source The photos
             * @return void
             */
            override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                imageList.add(onPhotosReturned(imageFiles)!!)
            }
            /**
             * Error when fetching the photos list
             *
             * @param error Errors
             * @param source The photos
             * @return void
             */
            override fun onImagePickerError(error: Throwable, source: MediaSource) {
                super.onImagePickerError(error, source)
            }
            /**
             * Cancel the process of fetching photos
             *
             * @param source The photos
             * @return void
             */
            override fun onCanceled(source: MediaSource) {
                super.onCanceled(source)
            }
        })
    }

    /**
     * Function to update the photos list in the database
     *
     * @param returnedPhotos Array list of photos
     * @return List of photos
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun onPhotosReturned(returnedPhotos: Array<MediaFile>):Image? {


        var list : Image?=null

        for (mediaFile in returnedPhotos) {
            val fileNameAsTempTitle = mediaFile.file.name
            val image = Image(
                imageTitle = fileNameAsTempTitle,
                imageUri = mediaFile.file.absolutePath,
                imageDate = LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))
            )

            // Update the database with the newly created object
            var id = insertData(image)
            image.id = id
            list = image
        }


                //photosDataset.add(image)
                //mPhotosAdapter.notifyDataSetChanged()


        return list

    }

    /**
     * Create and save the photo data
     *
     * @param image a photo
     * @return void
     */
    private fun insertData(image: Image): Int = runBlocking {
        val insertJob = photosViewModel!!.createNewPhoto(image)
        insertJob
    }

    /**
     * Save the location to the database
     *
     * @return void
     */
    private fun saveLocationToDB() {
        val location = Location(locationLongitude = tripLongitude, locationLatitude = tripLatitude,
            locationTitle = titleNameEditText.text.toString(), locationTripId = "",
            locationDescription = descriptionEditText.text.toString(), locationDate = "")//locationDate)
        MapCreatedActivity.locations.add(location)
        MapCreatedActivity.locationImages.add(Pair(location,imageList))
    }
}