package com.example.omy

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.omy.databinding.MainActivityBinding
import com.google.android.material.snackbar.Snackbar
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.IOException
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var ntButton: Button
    private lateinit var goButton: Button
    private lateinit var cancelButton: Button
    private lateinit var tnEditText: EditText
    private lateinit var weatherTemperatureText: TextView
    private lateinit var weatherIconView: ImageView
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        ntButton = findViewById(R.id.new_trip_button)
        goButton = findViewById(R.id.go_button)
        cancelButton = findViewById(R.id.cancel_button)
        tnEditText = findViewById(R.id.trip_name_edit_text)
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
                Snackbar.make(findViewById(R.id.notification_view),
                    R.string.enter_trip_name_notification, Snackbar.LENGTH_SHORT).show()
                it.showKeyboard()
            } else {
                Snackbar.make(findViewById(R.id.notification_view),
                    R.string.successfully_created_trip, Snackbar.LENGTH_SHORT
                ).show()
                tnEditText.text.clear()
            }
        }

        weatherTemperatureText = findViewById(R.id.weather_temperature)
        weatherIconView = findViewById(R.id.weather_icon)
        getCurrentWeather(weatherTemperatureText, weatherIconView)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm  = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm .hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun View.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val sheffield = LatLng(53.38, -1.46)
        // Tried zooming in, but no luck
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sheffield, 20.0f))
        mMap.addMarker(MarkerOptions().position(sheffield).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sheffield))
    }


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
                runOnUiThread {
                    try {
                        val json = JSONObject(body)
                        val responseObject: JSONObject = json.getJSONObject("current")
                        val tempC = responseObject.get("temp_c")
                        val weather = responseObject.getJSONObject("condition")
                        val icon = weather.get("icon")

                        textView.setText("${tempC.toString()}°C")
                        loadImage(imageView, "https:$icon")
                    } catch (e: JSONException) { e.printStackTrace() }
                }
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