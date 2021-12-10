package com.example.omy.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.omy.BuildConfig
import com.example.omy.R
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.Executors
import android.location.Location
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.omy.Communicator
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import java.text.DateFormat
import java.util.*

class HomeFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var mButtonStart: Button? = null
    private var mButtonEnd: Button? = null

    private lateinit var ntButton: Button
    private lateinit var goButton: Button
    private lateinit var cancelButton: Button
    private lateinit var tnEditText: EditText
    private lateinit var weatherTemperatureText: TextView
    private lateinit var weatherIconView: ImageView
    private lateinit var communicator: Communicator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        mButtonStart = view.findViewById<View>(R.id.button_start) as Button
        mButtonStart!!.setOnClickListener {
            startLocationUpdates()
            if (mButtonEnd != null) mButtonEnd!!.isEnabled = true
            mButtonStart!!.isEnabled = false
        }
        mButtonStart!!.isEnabled = true
        mButtonEnd = view.findViewById<View>(R.id.button_end) as Button
        mButtonEnd!!.setOnClickListener {
            stopLocationUpdates()
            if (mButtonStart != null) mButtonStart!!.isEnabled = true
            mButtonEnd!!.isEnabled = false
        }
        mButtonEnd!!.isEnabled = false

        /**/

        communicator = activity as Communicator
        ntButton = view.findViewById(R.id.new_trip_button)
        ntButton.setOnClickListener() {
            ntButton.visibility = View.GONE
            goButton.visibility = View.VISIBLE
            cancelButton.visibility = View.VISIBLE
            tnEditText.visibility = View.VISIBLE
        }
        goButton = view.findViewById(R.id.go_button)
        goButton.setOnClickListener {
            if (TextUtils.isEmpty(tnEditText.text.toString())) {
                Snackbar.make(view.findViewById(R.id.notification_view),
                    R.string.enter_trip_name_notification, Snackbar.LENGTH_SHORT).show()
                showSoftKeyboard(tnEditText)
            } else {
                Snackbar.make(view.findViewById(R.id.notification_view),
                    R.string.successfully_created_trip, Snackbar.LENGTH_SHORT
                ).show()
                closeKeyboard(tnEditText)
                communicator.passDataCom(tnEditText.text.toString())
            }
        }
        cancelButton = view.findViewById(R.id.cancel_button)
        cancelButton.setOnClickListener() {
            ntButton.visibility = View.VISIBLE
            goButton.visibility = View.GONE
            cancelButton.visibility = View.GONE
            tnEditText.visibility = View.GONE
            closeKeyboard(tnEditText)
        }
        tnEditText = view.findViewById(R.id.trip_name_edit_text)

        closeKeyboard(tnEditText)
        weatherTemperatureText = view.findViewById(R.id.weather_temperature)
        weatherIconView = view.findViewById(R.id.weather_icon)
        getCurrentWeather(weatherTemperatureText, weatherIconView)
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireContext() as Activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                ActivityCompat.requestPermissions(
                    requireContext() as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
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

    /**
     * it stops the location updates
     */
    private fun stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
    }

    override fun onResume() {
        super.onResume()
        mLocationRequest = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
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

            mMap.addMarker(
                MarkerOptions().position(
                    LatLng(mCurrentLocation!!.latitude, mCurrentLocation!!.longitude)
                ).title(mLastUpdateTime)
            )
            mMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(mCurrentLocation!!.latitude, mCurrentLocation!!.longitude), 14.0f
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
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14.0f))
    }

    companion object {
        private const val ACCESS_FINE_LOCATION = 123
    }


    private fun closeKeyboard(view: View) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken,0)
    }

    private fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    /* --- Set up the map --- */


    /*override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            }
        }
    }*/

    /* --- Get weather and temperature --- */
    private fun getCurrentWeather(textView: TextView, imageView: ImageView) {
        // TODO: Replace lat&long with actual geolocation
        val lat = 53.38
        val lon = -1.46

        val url = "http://api.weatherapi.com/v1/current.json?key=" + BuildConfig.WEATHER_APIKEY + "&q=" + lat + "," + lon
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body!!.string()
                print(body)
                try {
                    val json = JSONObject(body)
                    val responseObject: JSONObject = json.getJSONObject("current")
                    val tempC = responseObject.get("temp_c")
                    val weather = responseObject.getJSONObject("condition")
                    val icon = weather.get("icon")

                    textView.setText(context?.getString(R.string.weather_temperature, tempC.toString()))
                    loadImage(imageView, "https:$icon")
                } catch (e: JSONException) { e.printStackTrace() }
            }
            override fun onFailure(call: Call, e: IOException) { e.printStackTrace() }
        })
    }

    private fun loadImage(icon: ImageView, url: String) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var image: Bitmap? = null
        executor.execute {
            try {
                val onlineIcon = java.net.URL(url).openStream()
                image = BitmapFactory.decodeStream(onlineIcon)
                handler.post { icon.setImageBitmap(image) }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }
}