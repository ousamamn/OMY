package com.example.omy.locations

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.omy.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class LocationShowActivity : AppCompatActivity() {
    private lateinit var backButton: ImageView

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
    }
}