package com.example.omy.locations

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.omy.R
import com.example.omy.maps.MapAddActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class LocationShowActivity : AppCompatActivity() {
    private lateinit var backButton: ImageView
    private lateinit var seeAllReviewsButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_activity)
        val b: Bundle? = intent.extras

        var position = -1
        if (b != null) {
            position = b.getInt("position")
            if (position != -1) {
                val textView = findViewById<TextView>(R.id.title_name)
                val textViewTitleInfo = findViewById<TextView>(R.id.name_title_info)
                val textViewLongitudeInfo = findViewById<TextView>(R.id.longitude_info)
                val textViewLatitudeInfo = findViewById<TextView>(R.id.latitude_info)
                val element = LocationsAdapter.items[position]
                if (element != null) {
                    textView.text = element.titleLocation
                    textViewTitleInfo.text = element.titleLocation
                    textViewLongitudeInfo.text = element.longitude
                    textViewLatitudeInfo.text = element.latitude
                } else if (element == null) {
                    textView.text = "Error"
                    textViewTitleInfo.text = "Error"
                    textViewLongitudeInfo.text = "Error"
                    textViewLatitudeInfo.text = "Error"
                }
            }
        }

        val closeOptionsButton = findViewById<FloatingActionButton>(R.id.options_close_button)
        val openOptionsButton = findViewById<FloatingActionButton>(R.id.options_open_button)
        val editReviewButton = findViewById<FloatingActionButton>(R.id.edit_review_button)
        val addPhotoButton = findViewById<FloatingActionButton>(R.id.add_photo_button)
        backButton = findViewById(R.id.back_to_previous)
        seeAllReviewsButton = findViewById(R.id.see_all_reviews)
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
        seeAllReviewsButton.setOnClickListener {
            val intentForTitle = Intent(this, LocationSeeReviewActivity::class.java)
            val textView = findViewById<TextView>(R.id.title_name)
            val reviewActivityTitle = textView.text.toString()
            intentForTitle.putExtra("msg", reviewActivityTitle)
            startActivity(intentForTitle)
        }
        editReviewButton.setOnClickListener {
            val intentForEditReview = Intent(this, LocationEditReviewAcitivity::class.java)
            startActivity(intentForEditReview)
        }
    }
}