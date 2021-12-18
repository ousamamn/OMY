package com.example.omy.trips

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.omy.R
import com.example.omy.data.Trip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.omy.fragments.TripsFragment
import com.google.android.material.internal.ContextUtils.getActivity


class TripShowActivity : AppCompatActivity() {
    private lateinit var backButton: FloatingActionButton
    private lateinit var selectedTrip: Trip
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trip_activity)
        val b: Bundle? = intent.extras

        var position = ""

        if (b != null) {
            position = b.getString("position")!!
            for (trip in TripsAdapter.items){
                if (trip!!.id == position){
                    selectedTrip = trip
                }
            }

            if (position!="") {
                val tripTitle = findViewById<TextView>(R.id.trip_title)
                val tripDate = findViewById<TextView>(R.id.trip_date)
                val tripDistance = findViewById<TextView>(R.id.trip_distance)
                val tripLocation = findViewById<TextView>(R.id.trip_num_of_locations)
                val tripDescription = findViewById<TextView>(R.id.trip_description)
                val tripWeather = findViewById<TextView>(R.id.trip_weather)
                val element = selectedTrip
                //Log.i("showActivity", element.tripTitle!!)
                tripTitle.text = element.tripTitle
                tripDate.text = element.tripDate
                tripDistance.text = element.tripDistance.toString() + " km"
                tripDescription.text = element.tripDescription
                tripWeather.text = element.tripWeather.toString()
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