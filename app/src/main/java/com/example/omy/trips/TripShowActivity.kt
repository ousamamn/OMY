package com.example.omy.trips

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.omy.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.Marker




class TripShowActivity : AppCompatActivity(), OnMapReadyCallback, OnMarkerClickListener {
    private lateinit var mMap: GoogleMap

    private lateinit var tripMapTitle: String
    private lateinit var backButton: FloatingActionButton
    private val tripRoute : ArrayList<Pair<Double, Double>> = arrayListOf(
        Pair(53.38, -1.38),
        Pair(53.60, -1.38),
        Pair(53.72, -1.40),
        Pair(54.01, -1.31),
        Pair(54.09, -1.32),
        Pair(54.10, -1.29),
        Pair(54.12, -1.25),
        Pair(54.20, -1.20),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trip_activity)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        /*val toBeReplaced = arrayListOf(
            Pair(53.38, -1.38),
            Pair(53.60, -1.38),
            Pair(53.72, -1.40),
            Pair(54.01, -1.31),
            Pair(54.09, -1.32),
            Pair(54.10, -1.29),
            Pair(54.12, -1.25),
            Pair(54.20, -1.20),
        )*/
        //tripRoute = convertToLatLng(toBeReplaced)

        val b: Bundle? = intent.extras
        var position = -1
        if (b != null) {
            position = b.getInt("position")
            if (position != -1) {
                val tripTitle = findViewById<TextView>(R.id.trip_title)
                val tripDate = findViewById<TextView>(R.id.trip_date)
                val tripDistance = findViewById<TextView>(R.id.trip_distance)
                val tripLocation = findViewById<TextView>(R.id.trip_num_of_locations)
                val tripDescription = findViewById<TextView>(R.id.trip_description)
                val tripWeather = findViewById<TextView>(R.id.trip_weather)
                val element = TripsAdapter.items[position]
                Log.i("showActivity", element!!.tripTitle!!)
                tripTitle.text = element.tripTitle
                tripMapTitle = element.tripTitle.toString()
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

    override fun onMapReady(googleMap: GoogleMap) {
        val latLngLocations = convertToLatLng(tripRoute)
        val (firstLat, firstLong) = tripRoute.first()
        mMap = googleMap
        googleMap.setOnMarkerClickListener(this)

        val startingLocation = LatLng(firstLat, firstLong)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingLocation, 10.0f))

        // Draw a route
        val polylineOptions = PolylineOptions()
        polylineOptions.addAll(latLngLocations)
        polylineOptions.width(10f).color(R.color.lightgreen)
        mMap.addPolyline(polylineOptions)

        // For each location, display a marker for it
        for (loc in latLngLocations) {
            mMap.addMarker(MarkerOptions().position(loc)
                .title("This needs to be replaced with location title")
                .snippet(tripMapTitle))
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        // TODO: (Maybe I should add it too, but not sure since we'll have a list of locations down below)
        return false
    }

    private fun convertToLatLng(array: ArrayList<Pair<Double,Double>>): MutableList<LatLng> {
        return array.map { it ->
            val (latitude, longitude) = it
            LatLng(latitude, longitude) } as MutableList<LatLng>
    }
}