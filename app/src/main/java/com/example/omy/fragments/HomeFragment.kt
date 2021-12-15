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
import android.util.Log
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import android.content.Intent
import android.location.*
import android.widget.*
import com.example.omy.maps.MapCreatedActivity
import com.google.android.gms.location.LocationRequest
import java.text.DateFormat
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private lateinit var goButton: Button
    private lateinit var tnEditText: EditText
    private lateinit var weatherWidget: LinearLayout
    private lateinit var weatherTemperatureText: TextView
    private lateinit var weatherIconView: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mLocationRequest = LocationRequest.create()
        LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        startLocationUpdates()

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
                val intent = Intent(context, MapCreatedActivity::class.java)
                val extras = Bundle()
                extras.putString("trip_title", tnEditText.text.toString())
                extras.putString("trip_temperature", weatherTemperatureText.text.toString())
                intent.putExtras(extras)
                context?.startActivity(intent)
            }
        }

        tnEditText = view.findViewById(R.id.trip_name_edit_text)
        weatherWidget = view.findViewById(R.id.weather_widget)

        closeKeyboard(tnEditText)
        weatherTemperatureText = view.findViewById(R.id.weather_temperature)
        weatherIconView = view.findViewById(R.id.weather_icon)
    }

    @SuppressLint("MissingPermission")
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

    override fun onResume() {
        super.onResume()
        mLocationRequest = LocationRequest.create()

        LocationRequest.create().apply {
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

            getCurrentWeather(weatherTemperatureText, weatherIconView, mCurrentLocation.longitude, mCurrentLocation.latitude)
            weatherWidget.visibility = View.VISIBLE
        }
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

    /* --- Get weather and temperature --- */
    private fun getCurrentWeather(textView: TextView, imageView: ImageView, longitude: Double, latitude: Double) {

        val url =
            "http://api.weatherapi.com/v1/current.json?key=" +
                    BuildConfig.WEATHER_APIKEY + "&q=" + latitude + "," + longitude
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body!!.string()
                activity?.runOnUiThread {
                    try {
                        val json = JSONObject(body)
                        val responseObject: JSONObject = json.getJSONObject("current")
                        val tempC = responseObject.get("temp_c")
                        val weather = responseObject.getJSONObject("condition")
                        val icon = weather.get("icon")

                        textView.text = context?.getString(R.string.weather_temperature, tempC.toString())
                        loadImage(imageView, "https:$icon")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
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