package com.example.omy.maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.omy.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.Intent
import android.widget.Button


class MapCreatedActivity : AppCompatActivity() {


    private lateinit var displayTitle: TextView
    private lateinit var addButton: FloatingActionButton
    private lateinit var stopButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_created_trip)
        val b: Bundle? = intent.extras

        var msg: String? = "Title"
        if (b != null) {
            msg = b.getString("msg")
            displayTitle = findViewById(R.id.display_title)
            displayTitle.text = msg
        }
        addButton = findViewById(R.id.add_picture)
        stopButton = findViewById<Button>(R.id.stop_button)
        addButton.setOnClickListener() {
            val intent = Intent(this, MapAddActivity::class.java)
            val msg = displayTitle.text.toString()
            intent.putExtra("map_created", msg)
            startActivity(intent)
        }
    }
}