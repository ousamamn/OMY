package com.example.omy.maps

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.omy.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.omy.data.Image
import com.example.omy.data.ImageLocation
import com.example.omy.data.Trip
import com.example.omy.locations.LocationsViewModel
import com.example.omy.photos.PhotosAdapter
import com.example.omy.trips.TripShowActivity
import com.example.omy.trips.TripsAdapter
import com.example.omy.trips.TripsViewModel
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.create
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.gms.common.api.ApiException
import java.text.DateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import java.util.UUID

/*
* MapCreatedActivity.kt
* This file provides the users a Google map plus
  self-tracking function along with
  end the trip button, stop self-tracking button,
  title for the trip, temperature and add
  location name button.
* Mneimneh, Sekulski, Ooi 2021
* COM31007
*/
class MapCreatedActivity : AppCompatActivity(), OnMapReadyCallback {
    private var defaultLocation: Array<Double> = arrayOf(53.38, -1.48)
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var ctx: Context

    private lateinit var displayTitle: TextView
    private lateinit var displayTemperature: TextView
    private lateinit var startLocatingButton: Button
    private lateinit var stopLocatingButton: Button
    private lateinit var addButton: FloatingActionButton
    private lateinit var endTripButton: Button
    private var tripsViewModel: TripsViewModel? = null
    private var locationsViewModel: LocationsViewModel? = null
    private lateinit var tripWeather: String
    private lateinit var uuid: UUID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_created_trip)
        setActivity(this)
        setContext(this)

        // Receive information from HomeFragment
        tripsViewModel = ViewModelProvider(this)[TripsViewModel::class.java]
        locationsViewModel = ViewModelProvider(this)[LocationsViewModel::class.java]
        val b: Bundle? = intent.extras
        if (b != null) {
            displayTitle = findViewById(R.id.display_title)
            displayTitle.text = b.getString("trip_title")
            displayTemperature = findViewById(R.id.display_temperature)
            tripWeather = b.getString("trip_temperature")!!
            displayTemperature.text = getString(R.string.temperature, b.getString("trip_temperature"))
            visitedLongLatLocations.add(Pair(b.getDouble("base_latitude"),b.getDouble("base_longitude")))
        }

        // Display the Google map
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        // Start the locating function button
        startLocatingButton = findViewById<View>(R.id.map_start_location) as Button
        startLocatingButton.setOnClickListener {
            startLocationUpdates()
            startLocatingButton.text = getString(R.string.map_resume_location)
            startLocatingButton.visibility = View.GONE
            stopLocatingButton.visibility = View.VISIBLE
        }

        // Pause the locating function button
        stopLocatingButton = findViewById<View>(R.id.map_pause_location) as Button
        stopLocatingButton.setOnClickListener {
            stopLocationUpdates()
            stopLocatingButton.visibility = View.GONE
            startLocatingButton.visibility = View.VISIBLE
        }

        // End and save the trip to database button
        endTripButton = findViewById(R.id.map_end_trip)
        endTripButton.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Are you sure to finish and save the trip?")
                .setMessage("You will not be able to change your trip afterwards.")
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }.setPositiveButton("Yes") { dialog, which ->
                    stopLocationUpdates()
                    uuid = UUID.randomUUID()
                    saveTripToDB()

                    TripsAdapter.tripAndLocation[uuid.toString()]= locations
                    val intent = Intent(this, TripShowActivity::class.java)
                    val extras = Bundle()
                    extras.putString("position", uuid.toString())
                    extras.putInt("numOfLocations", locationImages.size)
                    intent.putExtras(extras)
                    for(i in 1..10000){
                        var i =0
                    }

                    // Pass parameters to the TripShowActivity
                    for (pair  in locationImages){
                        pair.first.locationTripId = uuid.toString()
                        val locationID = locationsViewModel!!.createNewLocation(pair.first)
                        for (image in pair.second){
                            Log.i("TRYUNG","GOTCHA"+pair.first.locationTitle)
                            var imageLocation = ImageLocation(imageId = image.id.toLong(), locationId = locationID.toLong())
                            locationsViewModel!!.createNewPhotoLocation(imageLocation)
                            PhotosAdapter.locationWithPhotos.add(Pair(image.id,Pair(pair.first.locationTitle,pair.first.locationDate)))
                        }
                    }
                    locations.clear()
                    locationImages.clear()
                    startActivity(intent)
                    finish()
                }.show()

        }

        // Add photo and its detail button
        addButton = findViewById(R.id.add_picture)
        addButton.setOnClickListener() {
            // Pass parameters to the MapAddActivity
            val intent = Intent(this, MapAddActivity::class.java)
            val extras = Bundle()
            extras.putString("trip_title", displayTitle.text.toString())
            val (tripLatitude,tripLongitude) = visitedLongLatLocations.last()
            extras.putDouble("trip_latitude", tripLatitude)
            extras.putDouble("trip_longitude",tripLongitude)
            extras.putString("location_date", LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm")))
            intent.putExtras(extras)
            startActivity(intent)
        }
    }

    /**
     * Check permissions for the map
     *
     * @return void
     */
    @SuppressLint("MissingPermission")
    private fun checkpermission(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_FINE_LOCATION)
            }
            return
        }
    }

    /**
     * Start updating the location
     *
     * @return void
     */
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val intent = Intent(ctx, LocationService::class.java)
        mLocationPendingIntent = PendingIntent.getService(ctx, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val locationTask = mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationPendingIntent!!)
        locationTask.addOnFailureListener { e ->
            if (e is ApiException) { e.message?.let { Log.w("MapsActivity", it) } }
            else { Log.w("MapsActivity", e.message!!) }
        }
        locationTask.addOnCompleteListener {
            Log.d("MapsActivity", "starting gps successful!")
        }
    }

    /**
     * Stop updating the location
     *
     * @return void
     */
    private fun stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationPendingIntent!!)
    }

    /**
     * Continue using the page
     *
     * @return void
     */
    override fun onResume() {
        super.onResume()
        mLocationRequest = create()
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 20000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkpermission()
        startLocationUpdates()
    }

    private var mCurrentLocation: Location? = null
    private var mLastUpdateTime: String? = null
    private var mLocationPendingIntent: PendingIntent? = null
    private val ACCESS_FINE_LOCATION = 123

    private var mLocationCallback: LocationCallback = object : LocationCallback() {
        /**
         * Give the information of the current location
         *
         * @param locationResult LocationResult's element
         * @return void
         */
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            mCurrentLocation = locationResult.lastLocation
            mLastUpdateTime = DateFormat.getTimeInstance().format(Date())
            val currentLongitude = mCurrentLocation!!.longitude
            val currentLatitude = mCurrentLocation!!.latitude

            mMap.addMarker(MarkerOptions()
                    .position(LatLng(currentLatitude, currentLongitude)).title(mLastUpdateTime)
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                LatLng(currentLatitude, currentLongitude), 16.0f)
            )

            visitedLongLatLocations.add(Pair(currentLatitude, currentLongitude))
        }
    }

    /**
     * Initialize the reviews data from database
     *
     * @param requestCode integer
     * @param permissions array of permissions
     * @param grantResults array of integer
     * @return void
     */
    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("Location", "Access permission granted")
                } else {
                    Log.e("Location ", "Denied")
                }
                return
            }
        }
    }

    /**
     * Display a map
     *
     * @param GoogleMap A GoogleMap object
     * @return void
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val sheffield = LatLng(defaultLocation[0], defaultLocation[1])
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sheffield, 16.0f))
    }

    /**
     * Set a context
     *
     * @param context A Context object
     * @return context
     */
    private fun setContext(context: Context) {
        ctx = context
    }

    companion object {
        private var activity: AppCompatActivity? = null
        private lateinit var mMap: GoogleMap
        var visitedLongLatLocations = ArrayList<Pair<Double, Double>>()

        /**
         * Fetch a specific activity
         *
         * @return activity
         */
        fun getActivity(): AppCompatActivity? {
            return activity
        }

        /**
         * Set up a specific activity
         *
         * @param newActivity an AppCompatActivity activity
         * @return activity
         */
        fun setActivity(newActivity: AppCompatActivity) {
            activity = newActivity
        }

        /**
         * Fetch a Google map
         *
         * @return a Google map
         */
        fun getMap(): GoogleMap {
            return mMap
        }
        var locations:MutableList<com.example.omy.data.Location?> = ArrayList()

        var locationImages:MutableList<Pair<com.example.omy.data.Location,List<Image>>> = ArrayList()
    }

    /**
     * Save trip to database
     *
     * @return void
     */
    private fun saveTripToDB() {
        val tripTitle = displayTitle.text.toString()
        val tripDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm"))
        val tripDistance = calculateDistance(visitedLongLatLocations)

        val coords = makeCoords(visitedLongLatLocations)
        val trip = Trip(id = uuid.toString(),tripTitle = tripTitle, tripDate = tripDate, tripDistance = tripDistance, tripWeather = tripWeather, tripListCoords = coords)


        tripsViewModel!!.createNewTrip(trip)

        TripsAdapter.items.add(trip)
    }

    /**
     * Calculate the distance in a trip
     *
     * @param visitedLatLongs List of latitude and longitude
     * @return the total distance of the trip
     */
    private fun calculateDistance(visitedLatLongs: List<Pair<Double,Double>>): Double {
        var sumOfDistances = 0.0F
        for (i in visitedLatLongs.indices) {
            if (i + 1 < visitedLatLongs.size) {
                val results = FloatArray(1)
                Location.distanceBetween(visitedLatLongs[i].first, visitedLatLongs[i].second,
                    visitedLatLongs[i+1].first, visitedLatLongs[i+1].second, results)
                sumOfDistances += results[0]
            }
        }
        return "%.2f".format(sumOfDistances / 1000).toDouble()
    }

    /**
     * Create the coordinate based on the latitude and longitude
     *
     * @param visitedLongLatLocations List of latitude and longitude
     * @return the coordinates of the location
     */
    private fun makeCoords(visitedLongLatLocations: List<Pair<Double, Double>>):String{
        var result =""
        for (coordinates in visitedLongLatLocations){
            result+= coordinates.first.toString() + "," + coordinates.second.toString() + '!'
        }
        return result
    }
}