package com.example.omy.locations

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.omy.R
import com.example.omy.data.Image
import com.example.omy.maps.MapAddActivity
import com.example.omy.photos.PhotosViewModel
import com.example.omy.trips.TripsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import pl.aprilapps.easyphotopicker.*
import java.util.ArrayList

class LocationShowActivity : AppCompatActivity() {
    private lateinit var backButton: ImageView
    private lateinit var seeAllReviewsButton: TextView
    private lateinit var seeAllPhotosButton: TextView
    private lateinit var easyImage: EasyImage
    private var imagesViewModel: PhotosViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_activity)
        imagesViewModel = ViewModelProvider(this)[PhotosViewModel::class.java]
        val b: Bundle? = intent.extras

        var position = -1
        if (b != null) {
            position = b.getInt("position")
            if (position != -1) {
                val textView = findViewById<TextView>(R.id.title_name)
                val textViewTitleInfo = findViewById<TextView>(R.id.location_title)
                val textViewLongitudeInfo = findViewById<TextView>(R.id.location_longitude)
                val textViewLatitudeInfo = findViewById<TextView>(R.id.location_latitude)
                val textViewDescription = findViewById<TextView>(R.id.location_description)
                //val idk = findViewById<ImageView>(R.id.photo)
                val element = LocationsAdapter.items[position]
                //val fabGallery: FloatingActionButton = findViewById(pl.aprilapps.easyphotopicker.R.id.fab_gallery)

                if (element != null) {
                    textView.text = element.locationTitle
                    textViewTitleInfo.text = element.locationTitle
                    textViewLongitudeInfo.text = element.locationLongitude.toString()
                    textViewLatitudeInfo.text = element.locationLatitude.toString()
                    textViewDescription.text = element.locationDescription

                } else if (element == null) {
                    textView.text = "Error"
                    textViewTitleInfo.text = "Error"
                    textViewLongitudeInfo.text = "Error"
                    textViewLatitudeInfo.text = "Error"
                }
            }
        }
        initEasyImage()

        val openOptionsButton = findViewById<FloatingActionButton>(R.id.options_open_button)
        val closeOptionsButton = findViewById<FloatingActionButton>(R.id.options_close_button)
        val editReviewButton = findViewById<FloatingActionButton>(R.id.edit_review_button)
        val addPhotoButton = findViewById<FloatingActionButton>(R.id.add_photo_button)
        backButton = findViewById(R.id.back_to_previous)
        seeAllReviewsButton = findViewById(R.id.location_reviews_see_all)
        seeAllPhotosButton = findViewById(R.id.location_photos_see_all)

        openOptionsButton.setOnClickListener {
            closeOptionsButton.visibility = View.VISIBLE
            editReviewButton.visibility = View.VISIBLE
            addPhotoButton.visibility = View.VISIBLE
            openOptionsButton.visibility = View.GONE

        }
        closeOptionsButton.setOnClickListener {
            closeOptionsButton.visibility = View.GONE
            editReviewButton.visibility = View.GONE
            addPhotoButton.visibility = View.GONE
            openOptionsButton.visibility = View.VISIBLE
        }
        backButton.setOnClickListener {
            onBackPressed()
            finish()
        }
        editReviewButton.setOnClickListener {
            val intentForEditReview = Intent(this, LocationEditReviewActivity::class.java)
            val textView = findViewById<TextView>(R.id.title_name)
            val reviewActivityTitle = textView.text.toString()
            intentForEditReview.putExtra("locationTitle", reviewActivityTitle)
            startActivity(intentForEditReview)
        }
        seeAllReviewsButton.setOnClickListener {
            val intentForTitle = Intent(this, LocationSeeReviewActivity::class.java)
            val textView = findViewById<TextView>(R.id.title_name)
            val reviewActivityTitle = textView.text.toString()
            intentForTitle.putExtra("locationTitle", reviewActivityTitle)
            startActivity(intentForTitle)
        }
        addPhotoButton.setOnClickListener {

            easyImage.openChooser(this@LocationShowActivity)
        }
//        seeAllReviewsButton.setOnClickListener {
//            val intentForTitle = Intent(this, LocationSeeReviewActivity::class.java)
//            val textView = findViewById<TextView>(R.id.title_name)
//            val reviewActivityTitle = textView.text.toString()
//            intentForTitle.putExtra("msg", reviewActivityTitle)
//            startActivity(intentForTitle)
//        }
    }

    private fun initEasyImage() {
        easyImage = EasyImage.Builder(this)
//        .setChooserTitle("Pick media")
//        .setFolderName(GALLERY_DIR)
            .setChooserType(ChooserType.CAMERA_AND_GALLERY)
            .allowMultiple(true)
//        .setCopyImagesToPublicGalleryFolder(true)
            .build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        easyImage.handleActivityResult(requestCode, resultCode, data, this,
            object : DefaultCallback() {
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
//            var id = insertData(imageData)
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