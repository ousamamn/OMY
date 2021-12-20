package com.example.omy.maps

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.omy.R
import com.example.omy.data.Location

class MapAddActivity : AppCompatActivity() {
    private lateinit var displayTitle: TextView
    private lateinit var latEditText: EditText
    private lateinit var longEditText: EditText
    private lateinit var cancelButton: Button
    private lateinit var saveButton: Button
    private lateinit var addPhotoFun: View

    private var tripLatitude: Double = 0.0
    private var tripLongitude: Double = 0.0

    private val intentPhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_created_location)



        /* Receive information from MapCreatedActivity */
        val b: Bundle? = intent.extras
        if (b != null) {
            displayTitle = findViewById(R.id.trip_title)
            displayTitle.text = b.getString("trip_title")
            tripLatitude = b.getDouble("trip_latitude")
            tripLongitude = b.getDouble("trip_longitude")
        }

        val titleNameEditText = findViewById<EditText>(R.id.location_title)
        val descriptionEditText = findViewById<EditText>(R.id.location_description)
        latEditText = findViewById(R.id.location_latitude)
        latEditText.hint = "%.2f".format(tripLatitude.toString())
        longEditText = findViewById(R.id.location_longitude)
        longEditText.hint = "%.2f".format(tripLongitude.toString())
        cancelButton = findViewById(R.id.location_cancel_button)
        saveButton = findViewById(R.id.location_add_button)
        addPhotoFun = findViewById(R.id.location_add_photo)
        
        cancelButton.setOnClickListener {
            onBackPressed()
            finish()
        }
        saveButton.setOnClickListener {
            if (TextUtils.isEmpty(titleNameEditText.text.toString())) {
                Toast.makeText(
                    applicationContext,
                    "Please insert the title",
                    Toast.LENGTH_SHORT
                )
                    .show();
            } else if (TextUtils.isEmpty(descriptionEditText.text.toString())) {
                Toast.makeText(
                    applicationContext,
                    "Please insert the description",
                    Toast.LENGTH_SHORT
                )
                    .show();
            } else {
                val location = Location(
                    locationLongitude = tripLongitude,
                    locationLatitude = tripLatitude,
                    locationTitle = titleNameEditText.text.toString(),
                    locationTripId = "", 
                    locationDescription = descriptionEditText.text.toString())
                MapCreatedActivity.locations.add(location)
                onBackPressed()
                finish()
            }
        }
        setupUI(findViewById(R.id.on_touch))
    }
    private fun hideSoftKeyboard(mapAddActivity: MapAddActivity) {
        val inputMethodManager: InputMethodManager = getSystemService(
            INPUT_METHOD_SERVICE
        ) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(
                currentFocus!!.windowToken,
                0
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupUI(view: View) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener(View.OnTouchListener { v, event ->
                hideSoftKeyboard(this)
                false
            })
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until (view as ViewGroup).childCount) {
                val innerView: View = (view as ViewGroup).getChildAt(i)
                setupUI(innerView)
            }
        }
        addPhotoFun.setOnClickListener {
            startActivityForResult(intentPhoto, 1)
        }
    }
}