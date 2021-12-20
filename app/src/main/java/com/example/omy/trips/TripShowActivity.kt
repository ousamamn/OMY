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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.Marker

class TripShowActivity : AppCompatActivity(), OnMapReadyCallback, OnMarkerClickListener {
    private lateinit var mMap: GoogleMap

    private lateinit var tripMapTitle: String
    private lateinit var backButton: FloatingActionButton
    private lateinit var selectedTrip: Trip
    private lateinit var tripRoute : MutableList<Pair<Double, Double>>
    private lateinit var element: Trip

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trip_activity)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        val b: Bundle? = intent.extras

        if (b != null) {
            val tripTitle = findViewById<TextView>(R.id.trip_title)
            val tripDate = findViewById<TextView>(R.id.trip_date)
            val tripDistance = findViewById<TextView>(R.id.trip_distance)
            val tripLocation = findViewById<TextView>(R.id.trip_num_of_locations)
            val tripDescription = findViewById<TextView>(R.id.trip_description)
            val tripWeather = findViewById<TextView>(R.id.trip_weather)

            if (b.getString("position").isNullOrBlank()){
                val position1 = b.getInt("position")
                if (position1!=-1) {
                    element = TripsAdapter.items[position1]!!
                    //Log.i("showActivity", element.tripTitle!!)
                }
            } else {
                val position = b.getString("position")!!
                for (trip in TripsAdapter.items) {
                    if (trip!!.id == position) { selectedTrip = trip }
                }
                if (position!="") {
                    element = selectedTrip
                    //Log.i("showActivity", element.tripTitle!!)
                }
            }
            tripTitle.text = element.tripTitle
            tripMapTitle = element.tripTitle.toString()
            tripDate.text = element.tripDate
            tripDistance.text = element.tripDistance.toString() + " km"
            tripDescription.text = element.tripDescription
            tripWeather.text = element.tripWeather.toString()
            tripRoute = parseCoords(element.tripListCoords!!)
            Log.e("PARSECOORDS", element.tripListCoords!!)
            //tripLocation.text = tripRoute.size.toString() NO THESE ARE DIFFERENT LOCATIONS
            //tripLocation.text = element!!.tripLocations!!.toString()
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

    private fun convertToLatLng(array: MutableList<Pair<Double,Double>>): MutableList<LatLng> {
        return array.map { val (latitude, longitude) = it
            LatLng(latitude, longitude) } as MutableList<LatLng>
    }
    private fun parseCoords(coords:String): MutableList<Pair<Double, Double>> {
        //Log.i("coords?!!!", coords)
        val coordListPair: MutableList<Pair<Double,Double>> = ArrayList()
        val coordsList = coords.split("!")

        for (coord in coordsList){
            if (coord.isNotBlank()) {
                val long = "%.2f".format(coord.split(",")[0]).toDouble()
                val lat = "%.2f".format(coord.split(",")[1]).toDouble()
                coordListPair.add(Pair(lat, long))
            }
        }
        return coordListPair
    }
}