package com.example.omy.trips

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.omy.R
import com.example.omy.data.Trip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class TripShowActivity : AppCompatActivity(), OnMapReadyCallback, OnMarkerClickListener {
    private lateinit var mMap: GoogleMap
    private lateinit var element: Trip

    private lateinit var tripMapTitle: String
    private lateinit var backButton: FloatingActionButton
    private lateinit var selectedTrip: Trip
    private lateinit var tripRoute : MutableList<Pair<Double, Double>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trip_activity)

        /* Display a Google map in the activity */
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        /* Get the data from the database and pass it into the activity */
        val b: Bundle? = intent.extras
        if (b != null) {
            val tripTitle = findViewById<TextView>(R.id.trip_title)
            val tripDate = findViewById<TextView>(R.id.trip_date)
            val tripDistance = findViewById<TextView>(R.id.trip_distance)
            val tripLocation = findViewById<TextView>(R.id.trip_num_of_locations)
            val tripWeather = findViewById<TextView>(R.id.trip_weather)

            if (b.getString("position").isNullOrBlank()){
                val position1 = b.getInt("position")
                if (position1!=-1) {
                    element = TripsAdapter.items[position1]!!
                }
            } else {
                val position = b.getString("position")!!
                for (trip in TripsAdapter.items) {
                    if (trip!!.id == position) { selectedTrip = trip }
                }
                if (position!="") {
                    element = selectedTrip
                }
            }
            tripTitle.text = element.tripTitle
            tripMapTitle = element.tripTitle.toString()
            tripDate.text = element.tripDate
            tripDistance.text = element.tripDistance.toString() + " km"
            tripWeather.text = element.tripWeather.toString()
            tripLocation.text = b.getInt("numOfLocations").toString()
            tripRoute = parseCoords(element.tripListCoords!!)
            Log.e("COORDS", element.tripListCoords!!)
        }

        /* Back to the previous activity function */
        backButton = findViewById(R.id.back_to_previous_button)
        backButton.setOnClickListener {
            onBackPressed()
            finish()
        }
    }

    // Function to create Google map
    override fun onMapReady(googleMap: GoogleMap) {
        val locations = TripsAdapter.tripAndLocation[element.id]
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
        locations!!.forEach { loc ->
            mMap.addMarker(MarkerOptions()
                .position(LatLng(loc!!.locationLatitude, loc.locationLongitude))
                .title(loc.locationTitle).snippet(tripMapTitle))
        }

        // Add a marker for the first and very last latlong value positions
        mMap.addMarker(MarkerOptions()
            .position(latLngLocations.first())
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            .title("Starting point").snippet(tripMapTitle))
        mMap.addMarker(MarkerOptions()
            .position(latLngLocations.last())
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            .title("Ending point").snippet(tripMapTitle))
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        // TODO: (Maybe I should add it too, but not sure since we'll have a list of locations down below)
        return false
    }

    // Function to convert the latitude and longitude
    private fun convertToLatLng(array: MutableList<Pair<Double,Double>>): MutableList<LatLng> {
        return array.map { val (latitude, longitude) = it
            LatLng(latitude, longitude) } as MutableList<LatLng>
    }

    // Function to store the coordinates based on the longitude and latitude
    private fun parseCoords(coords:String): MutableList<Pair<Double, Double>> {
        val coordListPairs: MutableList<Pair<Double,Double>> = ArrayList()
        val coordsList = coords.split("!")

        for (coord in coordsList){
            if (coord!="") {
                val lat = coord.split(",")[0].toDouble()
                val long = coord.split(",")[1].toDouble()
                coordListPairs.add(Pair(lat,long))
            }
        }
        return coordListPairs
    }
}