package com.example.omy.trips

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.omy.R

class TripShowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trip_activity)
        val b: Bundle? = intent.extras

        var position = -1
        if (b != null) {
            position = b.getInt("position")
            if (position != -1) {
                val tripTitle = findViewById<TextView>(R.id.title_name)
                val tripDate = findViewById<TextView>(R.id.date_info)
                val tripDistance = findViewById<TextView>(R.id.distance_info)
                val tripLocation = findViewById<TextView>(R.id.numOfLocations_info)
                val element = TripsAdapter.items[position]
                tripTitle.text = element.title
                tripDate.text = element.date
                tripDistance.text = element.distance + " km"
                tripLocation.text = element.numOfLocations
            }
        }
    }
}