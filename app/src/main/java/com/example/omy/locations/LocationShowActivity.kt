package com.example.omy.locations

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.omy.R
import com.example.omy.locations.LocationsAdapter.Companion.items

class LocationShowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_show_activity)
        val b: Bundle? = intent.extras

        var position = -1
        if (b != null) {
            position = b.getInt("position")
            if (position != -1) {
                val textView = findViewById<TextView>(R.id.title_name)
                val element = LocationsAdapter.items[position]
                if (element != null) {
                    textView.text = element.titleLocation
                } else if (element == null) {
                    textView.text = "Error"
                }
            }
        }
    }
}