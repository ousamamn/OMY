package com.example.omy

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.service.autofill.UserData
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.omy.databinding.MainActivityBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val ntButton: Button = findViewById(R.id.new_trip_button)
        val goButton: Button = findViewById(R.id.go_button)
        val cancelButton: Button = findViewById(R.id.cancel_button)
        val tnEditText: EditText = findViewById(R.id.trip_name_edit_text)
        ntButton.setOnClickListener() {
            ntButton.visibility = View.GONE
            goButton.visibility = View.VISIBLE
            cancelButton.visibility = View.VISIBLE
            tnEditText.visibility = View.VISIBLE
        }
        cancelButton.setOnClickListener() {
            ntButton.visibility = View.VISIBLE
            goButton.visibility = View.GONE
            cancelButton.visibility = View.GONE
            tnEditText.visibility = View.GONE
        }
        goButton.setOnClickListener() {
            if (TextUtils.isEmpty(tnEditText.text.toString())) {
                Snackbar.make(
                    findViewById(R.id.notification_view),
                    R.string.enter_trip_name_notification,
                    Snackbar.LENGTH_SHORT
                ).show()
                it.showKeyboard()
            } else {
                Snackbar.make(
                    findViewById(R.id.notification_view),
                    R.string.successfully_created_trip,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        getCurrentWeather()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment mapFragment.getMapAsync(this)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null ) {
            val imm  = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm .hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun View.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
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
        mMap.uiSettings.isZoomControlsEnabled = false

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun getCurrentWeather() {

        val textView = findViewById<TextView>(R.id.api)

        //var client = OkHttpClient()
        //var request = OkHttpRequest(client)
        val client = OkHttpClient()


        // Make a GET request to locationURL to obtain a location key
        val lat = 53.38
        val lon = -1.46

        val url = "http://api.weatherapi.com/v1/current.json?key=" + BuildConfig.WEATHER_APIKEY + "&q=" + lat + "," + lon

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                //println(body)
                //val gson = GsonBuilder().create()
                //val data = gson.fromJson(body, UserData::class.java)
                //println(data)
                textView.text = body
            }
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })

        // Make another GET request to get current weather
        /**
        val weatherRequest = StringRequest(
        Request.Method.GET, weatherURL + er["Key"],
        { response -> textView.text = response },
        { textView.text = "Error"})
        )*/
    }
}