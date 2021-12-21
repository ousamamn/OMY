package com.example.omy.locations

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.omy.R
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_activity)
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

        addReviewButton = findViewById(R.id.location_review_add)
        addPhotoButton = findViewById(R.id.location_photo_add)
        backButton = findViewById(R.id.back_to_previous)
        seeAllReviewsButton = findViewById(R.id.location_reviews_see_all)
        seeAllPhotosButton = findViewById(R.id.location_photos_see_all)

        backButton.setOnClickListener {
            onBackPressed()
            finish()
        }
        addReviewButton.setOnClickListener {
            val intentAddLocationReview = Intent(this, LocationAddReviewActivity::class.java)
            val textView = findViewById<TextView>(R.id.title_name)
            val reviewActivityTitle = textView.text.toString()
            intentAddLocationReview.putExtra("locationTitle", reviewActivityTitle)
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
                //.setMemento(memento)
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
}