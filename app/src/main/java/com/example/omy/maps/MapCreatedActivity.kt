package com.example.omy.maps

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
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
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import com.example.omy.BuildConfig
import com.example.omy.fragments.HomeFragment
import com.google.android.gms.location.*
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
import java.text.DateFormat
import java.util.*
import java.util.concurrent.Executors


class MapCreatedActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private var defaultLocation: Array<Double> = arrayOf(53.38, -1.46)
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private lateinit var displayTitle: TextView
    private lateinit var displayTemperature: TextView
    private lateinit var startLocatingButton: Button
    private lateinit var stopLocatingButton: Button
    private lateinit var addButton: FloatingActionButton
    private lateinit var endTripButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_created_trip)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        startLocatingButton = findViewById<View>(R.id.map_start_location) as Button
        startLocatingButton.setOnClickListener {
            startLocationUpdates()
            startLocatingButton.text = getString(R.string.map_resume_location)
            stopLocatingButton.isEnabled = true
            startLocatingButton.isEnabled = false
        }
        startLocatingButton.isEnabled = true
        stopLocatingButton = findViewById<View>(R.id.map_pause_location) as Button
        stopLocatingButton.setOnClickListener {
            stopLocationUpdates()
            startLocatingButton.isEnabled = true
            stopLocatingButton.isEnabled = false
        }
        stopLocatingButton.isEnabled = false

        endTripButton = findViewById<Button>(R.id.map_end_trip)
        endTripButton.setOnClickListener {
            stopLocationUpdates()
            saveToDB()
            val intent = Intent(this, MapAddActivity::class.java)
            val title = displayTitle.text.toString()
            intent.putExtra("map_created", title)
            startActivity(intent)
        }


        /* Receive information from HomeFragment */
        val b: Bundle? = intent.extras
        if (b != null) {
            displayTitle = findViewById(R.id.display_title)
            displayTitle.text = b.getString("trip_title")
            displayTemperature = findViewById(R.id.display_temperature)
            displayTemperature.text = getString(R.string.temperature, b.getString("trip_temperature"))
        }
        addButton = findViewById(R.id.add_picture)
        addButton.setOnClickListener() {
            val intent = Intent(this, MapAddActivity::class.java)
            intent.putExtra("trip_title", displayTitle.text.toString())
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
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this as Activity,
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
            null  /*Looper*/
        )
    }

    private fun stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
    }

    override fun onResume() {
        super.onResume()
        mLocationRequest = LocationRequest.create()
        Log.e("Z", "ZUK")
        LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        startLocationUpdates()
    }

    private lateinit var mCurrentLocation: Location
    private lateinit var mLastUpdateTime: String
    private var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            mCurrentLocation = locationResult.getLastLocation()
            mLastUpdateTime = DateFormat.getTimeInstance().format(Date())
            Log.i("MAP", "new location " + mCurrentLocation.toString())

            if (mMap != null) mMap.addMarker(
                MarkerOptions().position(
                    LatLng(mCurrentLocation!!.latitude, mCurrentLocation!!.longitude)
                ).title(mLastUpdateTime)
            )
            mMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(mCurrentLocation!!.latitude, mCurrentLocation!!.longitude), 16.0f
                )
            )
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

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
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
        //mMap.uiSettings.isZoomControlsEnabled = true
        val location = LatLng(defaultLocation[0], defaultLocation[1])
        //mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16.0f))
    }

    companion object {
        private const val ACCESS_FINE_LOCATION = 123
    }

    private fun saveToDB() {
        TODO("Connect to DAO and save the trip")
    }
}