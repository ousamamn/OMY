package com.example.omy.fragments

import android.Manifest
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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.omy.BuildConfig
import com.example.omy.R
import com.example.omy.TripsActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.Executors
import android.content.IntentSender
import com.example.omy.Communicator
import android.content.Intent as Intent

class HomeFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var ntButton: Button
    private lateinit var goButton: Button
    private lateinit var cancelButton: Button
    private lateinit var tnEditText: EditText
    private lateinit var weatherTemperatureText: TextView
    private lateinit var weatherIconView: ImageView
    private lateinit var communicator: Communicator

    private val REQUEST_LOCATION_PERMISSION = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        communicator = activity as Communicator
        ntButton = view.findViewById(R.id.new_trip_button)
        ntButton.setOnClickListener() {
            ntButton.visibility = View.GONE
            goButton.visibility = View.VISIBLE
            cancelButton.visibility = View.VISIBLE
            tnEditText.visibility = View.VISIBLE
        }

        goButton = view.findViewById(R.id.go_button)
        goButton.setOnClickListener() {
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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val sheffield = LatLng(53.38, -1.46)
        enableMyLocation()

        // Tried zooming in, but no luck
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sheffield, 15f))
        mMap.addMarker(MarkerOptions().position(sheffield).title("Marker in Sydney"))
    }

    private fun isPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            mMap.isMyLocationEnabled = true
        }
        else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            }
        }
    }

    //* --- Get weather and temperature --- *//*
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
                //runOnUiThread {
                    try {
                        val json = JSONObject(body)
                        val responseObject: JSONObject = json.getJSONObject("current")
                        val tempC = responseObject.get("temp_c")
                        val weather = responseObject.getJSONObject("condition")
                        val icon = weather.get("icon")

                        textView.setText("${tempC.toString()}°C")
                        loadImage(imageView, "https:$icon")
                    } catch (e: JSONException) { e.printStackTrace() }
                //}
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