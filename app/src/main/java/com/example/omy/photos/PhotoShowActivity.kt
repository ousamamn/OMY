package com.example.omy.photos

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.omy.R
import com.example.omy.data.Image
import com.example.omy.locations.LocationsViewModel

/*
* PhotoShowActivity.kt
* This file provides the selected photo
  and its details for the users
* Mneimneh, Sekulski, Ooi 2021
* COM31007
*/
class PhotoShowActivity : FragmentActivity() {
    private lateinit var backButton: ImageView
    private var locationsViewModel: LocationsViewModel? = null

    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val position = result.data?.getIntExtra("position", -1)
            val id = result.data?.getIntExtra("id", -1)
            val del_flag = result.data?.getIntExtra("deletion_flag", -1)
            var intent = Intent().putExtra("position", position)
                .putExtra("id", id).putExtra("deletion_flag", del_flag)
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    this.setResult(result.resultCode, intent)
                    this.finish()
                }
            }
        }//location_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_activity)

        locationsViewModel = ViewModelProvider(this)[LocationsViewModel::class.java]

        // Get the data from the database and pass it into the activity
        val b: Bundle? = intent.extras
        var position = -1
        if (b != null) {
            // this is the image position in the itemList
            position = b.getInt("position")
        }
        displayData(position)

        // Back to previous page
        backButton = findViewById(R.id.back_to_previous)
        backButton.setOnClickListener {
            onBackPressed()
            finish()
        }
    }

    private fun displayData(position: Int) {
        if (position != -1) {
            val imageView = findViewById<ImageView>(R.id.photo_image)
            val title = findViewById<TextView>(R.id.photo_title)
            val descriptionTextView = findViewById<TextView>(R.id.description_detail)
            val dateTextView = findViewById<TextView>(R.id.photo_date)
            val locationTextView = findViewById<TextView>(R.id.location_detail)
            val imageData = PhotosAdapter.items[position]
            //Log.i("PHOTOS", imageData.thumbnail.toString())

            imageView.setImageBitmap(imageData.thumbnail)
            title.text = PhotosAdapter.items[position].imageTitle
            descriptionTextView.text = PhotosAdapter.items[position].imageDescription
            dateTextView.text =
                PhotosAdapter.locationWithPhotos[PhotosAdapter.items[position].id - 1].second.second
            locationTextView.text =
                PhotosAdapter.locationWithPhotos[PhotosAdapter.items[position].id - 1].second.first
        }
    }
        /**
         * Retrieve location names, populate PhotoDetail activity with Photo's data from the database
         *
         * @param position Photo's position/id
         * @return void
         */
        private fun displayData(position: Int) {
            var photoLocationsList = ArrayList<Pair<Image, String?>>()
            this.locationsViewModel!!.getLocationPhotosToDisplay()!!
                .observe(this, { newValueLocations ->
                    Log.v("bb", PhotosAdapter.items.toString())
                    for (loc in newValueLocations) {
                        val locationTitle = loc.location.locationTitle
                        val imageList = loc.imageIdList
                        for (image in imageList) {
                            photoLocationsList.add(Pair(image, locationTitle))
                        }
                    }

                    if (position != -1) {
                        val imageView = findViewById<ImageView>(R.id.photo_image)
                        val title = findViewById<TextView>(R.id.photo_title)
                        val dateTextView = findViewById<TextView>(R.id.photo_date)
                        val imageLocationName = findViewById<TextView>(R.id.location_detail)
                        val imageData = PhotosAdapter.items[position]

                        imageView.setImageBitmap(imageData.thumbnail)
                        title.text = PhotosAdapter.items[position].imageTitle
                        dateTextView.text = PhotosAdapter.items[position].imageDate
                        imageLocationName.text =
                            getLocationName(PhotosAdapter.items[position], photoLocationsList)
                    }
                })
        }

        /**
         * Based on the photo details, return its location's name (helper function)
         *
         * @param photo Image object
         * @param photoLocPairs A pair list of photos and location names
         * @return Name of the location the photo belongs to
         */
        private fun getLocationName(
            photo: Image,
            photoLocPairs: ArrayList<Pair<Image, String?>>
        ): String {
            var locationTitle = ""
            for (pair in photoLocPairs) {
                if (photo == pair.first) {
                    locationTitle = pair.second.toString()
                }
            }
            return locationTitle
        }
    }