package com.example.omy.fragments

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.omy.R
import com.google.android.gms.maps.OnMapReadyCallback

class MapFragmentActivity : AppCompatActivity() {
    private lateinit var displayTitle: TextView
    private lateinit var backToPrevious: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_created_trip)
        val b: Bundle? = intent.extras

        val msg: String?
        if (b != null) {
            msg = b.getString("msg")
            displayTitle = findViewById(R.id.display_title)
            displayTitle.text = msg
        }
        backToPrevious = findViewById(R.id.stop_button)
        backToPrevious.setOnClickListener {
            onBackPressed()
            finish()
        }
    }
}