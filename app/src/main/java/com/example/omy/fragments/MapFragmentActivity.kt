package com.example.omy.fragments

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.omy.R
import com.google.android.gms.maps.OnMapReadyCallback

class MapFragmentActivity : AppCompatActivity(), OnMapReadyCallback {

    private var displayMessage: String? = ""
    private lateinit var displayTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Receive the trip title and display a toast
        displayMessage = arguments?.getString("message")
        displayTitle = view.findViewById(R.id.display_title)
        displayTitle.text = displayMessage
    }
}