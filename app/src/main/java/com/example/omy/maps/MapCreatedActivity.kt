package com.example.omy.maps

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.omy.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.omy.BuildConfig
import com.example.omy.data.Trip
import com.example.omy.fragments.HomeFragment
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
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URI.create
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt


class MapCreatedActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private var defaultLocation: Array<Double> = arrayOf(53.38, -1.48)
    private var visitedLongLatLocations = ArrayList<Pair<Double, Double>>()
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private lateinit var displayTitle: TextView
    private lateinit var displayTemperature: TextView
    private lateinit var startLocatingButton: Button
    private lateinit var stopLocatingButton: Button
    private lateinit var addButton: FloatingActionButton
    private lateinit var endTripButton: Button
    private var tripsViewModel: TripsViewModel? = null
    private var tripID:Int =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_created_trip)

        /* Receive information from HomeFragment */
        tripsViewModel = ViewModelProvider(this)[TripsViewModel::class.java]
        val b: Bundle? = intent.extras
        if (b != null) {
            displayTitle = findViewById(R.id.display_title)
            displayTitle.text = b.getString("trip_title")
            displayTemperature = findViewById(R.id.display_temperature)
            displayTemperature.text = getString(R.string.temperature, b.getString("trip_temperature"))
            visitedLongLatLocations.add(Pair(b.getDouble("base_latitude"),b.getDouble("base_longitude")))
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        startLocatingButton = findViewById<View>(R.id.map_start_location) as Button
        startLocatingButton.setOnClickListener {
            startLocationUpdates()
            startLocatingButton.text = getString(R.string.map_resume_location)
            startLocatingButton.visibility = View.GONE
            stopLocatingButton.visibility = View.VISIBLE
        }
        stopLocatingButton = findViewById<View>(R.id.map_pause_location) as Button
        stopLocatingButton.setOnClickListener {
            stopLocationUpdates()
            stopLocatingButton.visibility = View.GONE
            startLocatingButton.visibility = View.VISIBLE
        }

        endTripButton = findViewById<Button>(R.id.map_end_trip)
        endTripButton.setOnClickListener {
            stopLocationUpdates()
            saveTripToDB()
            /* Pass parameters to the TripShowActivity */
            val intent = Intent(this, TripShowActivity::class.java)
            val extras = Bundle()
            //extras.putString("position", tripID)    // HOPEFULLY IT IS POSSIBLE TO FETCH A TRIP USING ITS TITLE
            tripsViewModel!!.getLastTrip()!!.observe(this, {
                    newValue ->
                intent.putExtra("position",newValue!!.id-1)
                startActivity(intent)
            })
            //Log.i("CHECK",tripID.toString())

        }

        addButton = findViewById(R.id.add_picture)
        addButton.setOnClickListener() {
            /* Pass parameters to the MapAddActivity */
            val intent = Intent(this, MapAddActivity::class.java)
            val extras = Bundle()
            extras.putString("trip_title", displayTitle.text.toString())
            val (tripLongitude, tripLatitude) = visitedLongLatLocations.last()
            extras.putDouble("trip_longitude",tripLongitude)
            extras.putDouble("trip_latitude", tripLatitude)
            intent.putExtras(extras)
            startActivity(intent)
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    ACCESS_FINE_LOCATION
                )
            }
            return
        }

        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            null /* Looper */
        )
    }

    private fun stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
    }

    override fun onResume() {
        super.onResume()
        mLocationRequest = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 20000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        startLocationUpdates()
    }

    private var mCurrentLocation: Location? = null
    private var mLastUpdateTime: String? = null
    private var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            mCurrentLocation = locationResult.lastLocation
            mLastUpdateTime = DateFormat.getTimeInstance().format(Date())
            Log.i("MAP", "new location " + mCurrentLocation.toString())
            val currentLongitude = mCurrentLocation!!.longitude
            val currentLatitude = mCurrentLocation!!.latitude

            mMap.addMarker(MarkerOptions()
                    .position(LatLng(currentLatitude, currentLongitude)).title(mLastUpdateTime)
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                LatLng(currentLatitude, currentLongitude), 16.0f)
            )

            visitedLongLatLocations.add(Pair(currentLatitude, currentLongitude))
            Log.i("Locations", visitedLongLatLocations.toString())
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    mFusedLocationClient.requestLocationUpdates(
                        mLocationRequest,
                        mLocationCallback, null /* Looper */
                    )
                }
                return
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val sheffield = LatLng(defaultLocation[0], defaultLocation[1])
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sheffield, 14.0f))
    }

    companion object {
        private const val ACCESS_FINE_LOCATION = 123
    }

    private fun saveTripToDB() {
        //TODO("Connect to DAO and save the trip")
        val tripTitle = displayTitle.text.toString()

        val tripDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm"))

        val tripDistance = calculateDistance(visitedLongLatLocations)

        val tripWeather = getWeather(visitedLongLatLocations[0])

        val trip = Trip(tripTitle = tripTitle, tripDate = tripDate, tripDistance = tripDistance, tripWeather = tripWeather, tripDescription = "")

        tripsViewModel!!.createNewTrip(trip)!!

        TripsAdapter.items.add(trip)
        //Log.i("ID_ATTEMPT", tripID.toString())

    }

    private fun calculateDistance(visitedLongLatLocations: List<Pair<Double,Double>>):Double{

        var (first_long,first_lat) = visitedLongLatLocations.first()
        var (last_long,last_lat) = visitedLongLatLocations.last()
        var start = Location ("startLocation")
        start.latitude = first_lat
        start.longitude = first_long
        var end = Location("endLocation")
        end.latitude = last_lat
        end.longitude = last_long
        return start.distanceTo(end).toDouble()
    }

    private fun getWeather(coords:Pair<Double,Double>):Int{
        val (longitude,latitude) = coords
            val url =
                "http://api.weatherapi.com/v1/current.json?key=" +
                        BuildConfig.WEATHER_APIKEY + "&q=" + latitude + "," + longitude
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
        var temp = 999
            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body!!.string()
                    //activity?.runOnUiThread {
                        try {
                            val json = JSONObject(body)
                            val responseObject: JSONObject = json.getJSONObject("current")
                            val tempC = responseObject.get("temp_c")
                            val weather = responseObject.getJSONObject("condition")
                            val icon = weather.get("icon")
                            temp = tempC.toString().toDouble().roundToInt()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    //}
                }

                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }
            })
        return temp
        }
    }