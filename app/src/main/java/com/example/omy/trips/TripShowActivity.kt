package com.example.omy.trips

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.omy.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.omy.fragments.TripsFragment
import com.google.android.material.internal.ContextUtils.getActivity


class TripShowActivity : AppCompatActivity() {
    private lateinit var backButton: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trip_activity)
        val b: Bundle? = intent.extras

        var position = -1
        if (b != null) {
            position = b.getInt("position")
            if (position != -1) {
                val tripTitle = findViewById<TextView>(R.id.trip_title)
                val tripDate = findViewById<TextView>(R.id.trip_date)
                val tripDistance = findViewById<TextView>(R.id.trip_distance)
                val tripLocation = findViewById<TextView>(R.id.trip_num_of_locations)
                val element = TripsAdapter.items[position]
                Log.i("showActivity", element!!.tripTitle!!)
                tripTitle.text = element!!.tripTitle!!
                tripDate.text = element!!.tripDate!!
                tripDistance.text = element!!.tripDistance!!.toString() + " km"
                //tripLocation.text = element!!.tripLocations!!.toString()
            }
        }
        backButton = findViewById(R.id.back_to_previous_button)
        backButton.setOnClickListener {
            onBackPressed()
            finish()
        }
    }
}