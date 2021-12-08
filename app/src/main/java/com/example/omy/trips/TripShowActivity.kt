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
                val textView = findViewById<TextView>(R.id.title_name)
                val textViewDate = findViewById<TextView>(R.id.date_info)
                val textViewDistance = findViewById<TextView>(R.id.distance_info)
                val textViewLocation = findViewById<TextView>(R.id.numOfLocations_info)
                val element = TripsAdapter.items[position]
                if (element != null) {
                    textView.text = element.title
                    textViewDate.text = element.date
                    textViewDistance.text = element.distance + " km"
                    textViewLocation.text = element.numOfLocations
                } else if (element == null) {
                    textView.text = "Error"
                    textViewDate.text = "Error"
                    textViewDistance.text = "Error"
                    textViewLocation.text = "Error"
                }
            }
        }
    }
}