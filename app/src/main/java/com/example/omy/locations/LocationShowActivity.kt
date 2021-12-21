package com.example.omy.locations

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.omy.R
import com.example.omy.data.Image
import com.example.omy.photos.PhotosViewModel
import kotlinx.coroutines.runBlocking
import pl.aprilapps.easyphotopicker.*
import java.util.ArrayList
import com.example.omy.reviews.LocationAddReviewActivity
import com.example.omy.reviews.LocationReviewsActivity
import pl.aprilapps.easyphotopicker.ChooserType
import pl.aprilapps.easyphotopicker.EasyImage

class LocationShowActivity : AppCompatActivity() {
    private lateinit var backButton: ImageView
    private lateinit var addReviewButton: TextView
    private lateinit var addPhotoButton: TextView
    private lateinit var seeAllReviewsButton: TextView
    private lateinit var seeAllPhotosButton: TextView
    private lateinit var easyImage: EasyImage
    private var imagesViewModel: PhotosViewModel? = null
    private var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_activity)
        imagesViewModel = ViewModelProvider(this)[PhotosViewModel::class.java]
        val b: Bundle? = intent.extras

        if (b != null) {
            position = b.getInt("position")
            if (position != -1) {
                Log.e("loc_position", position.toString())
                val textView = findViewById<TextView>(R.id.title_name)
                val textViewTitleInfo = findViewById<TextView>(R.id.location_title)
                val textViewLongitudeInfo = findViewById<TextView>(R.id.location_longitude)
                val textViewLatitudeInfo = findViewById<TextView>(R.id.location_latitude)
                val textViewDescription = findViewById<TextView>(R.id.location_description)
                val element = LocationsAdapter.items[position]

                if (element != null) {
                    textView.text = element.locationTitle
                    textViewTitleInfo.text = element.locationTitle
                    textViewLongitudeInfo.text = element.locationLongitude.toString()
                    textViewLatitudeInfo.text = element.locationLatitude.toString()
                    textViewDescription.text = element.locationDescription

                } else {
                    textView.text = "Error"
                    textViewTitleInfo.text = "Error"
                    textViewLongitudeInfo.text = "Error"
                    textViewLatitudeInfo.text = "Error"
                }
            }
        }

        backButton = findViewById(R.id.back_to_previous)
        addReviewButton = findViewById(R.id.location_review_add)
        seeAllReviewsButton = findViewById(R.id.location_reviews_see_all)
        seeAllPhotosButton = findViewById(R.id.location_photos_see_all)
        addPhotoButton = findViewById(R.id.location_photo_add)

        backButton.setOnClickListener {
            onBackPressed()
            finish()
        }
        addReviewButton.setOnClickListener {
            val intentAddLocationReview = Intent(this, LocationAddReviewActivity::class.java)
            val textView = findViewById<TextView>(R.id.title_name)
            val reviewActivityTitle = textView.text.toString()
            intentAddLocationReview.putExtra("locationTitle", reviewActivityTitle)
            intentAddLocationReview.putExtra("locationPosition", position)
            startActivity(intentAddLocationReview)
        }
        seeAllReviewsButton.setOnClickListener {
            val intentForTitle = Intent(this, LocationReviewsActivity::class.java)
            val textView = findViewById<TextView>(R.id.title_name)
            val reviewActivityTitle = textView.text.toString()
            intentForTitle.putExtra("locationTitle", reviewActivityTitle)
            startActivity(intentForTitle)
        }
        addPhotoButton.setOnClickListener {
            val easyImage: EasyImage = EasyImage.Builder(this)
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .setCopyImagesToPublicGalleryFolder(false)
                .setFolderName("EasyImage sample")
                //.allowMultiple(true)
                .build()

            easyImage.openGallery(this)

            //TODO("SAVE PICKED PHOTO")
        }
        seeAllPhotosButton.setOnClickListener {
            val intentForTitle = Intent(this, LocationReviewsActivity::class.java)
            val textView = findViewById<TextView>(R.id.title_name)
            val reviewActivityTitle = textView.text.toString()
            intentForTitle.putExtra("locationTitle", reviewActivityTitle)
            startActivity(intentForTitle)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        easyImage.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {
            override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                onPhotosReturned(imageFiles)
            }

            override fun onImagePickerError(error: Throwable, source: MediaSource) {
                super.onImagePickerError(error, source)
            }

            override fun onCanceled(source: MediaSource) {
                super.onCanceled(source)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onPhotosReturned(returnedPhotos: Array<MediaFile>) {
       // myDataset.addAll(getImageData(returnedPhotos))

        // we tell the adapter that the data is changed and hence the grid needs
        Log.i("TAG",returnedPhotos[0].file.absolutePath)
    }

    private fun getImageData(returnedPhotos: Array<MediaFile>): List<Image> {
        val imageList: MutableList<Image> = ArrayList<Image>()
        for (mediaFile in returnedPhotos) {
            val fileNameAsTempTitle = mediaFile.file.name
            var image = Image(
                imageTitle = fileNameAsTempTitle,
                imageUri = mediaFile.file.absolutePath
            )
            // Update the database with the newly created object
            var id = insertData(image)
            image.id = id
            imageList.add(image)
        }
        return imageList
    }

    private fun insertData(image: Image): Int = runBlocking {
        var insertJob = imagesViewModel!!.createNewPhoto(image)
        insertJob
    }

}