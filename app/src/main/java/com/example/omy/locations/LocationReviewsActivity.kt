package com.example.omy.locations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.omy.R

class LocationReviewsActivity : AppCompatActivity() {
    private lateinit var displayTitle: TextView
    private lateinit var backToPrevious: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_reviews_activity)
        val b: Bundle? = intent.extras

        var msg: String? = "Title"
        if (b != null) {
            msg = b.getString("locationTitle")
            displayTitle = findViewById(R.id.title_name)
            displayTitle.text = msg
        }
        backToPrevious = findViewById(R.id.back_to_previous)
        backToPrevious.setOnClickListener {
            onBackPressed()
            finish()
        }
    }
}