package com.example.omy.maps

import android.content.Intent
import android.icu.text.CaseMap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.omy.R

class MapAddActivity : AppCompatActivity() {
    private lateinit var displayTitle: TextView
    private lateinit var cancelButton: Button
    private lateinit var saveButton: Button
    private lateinit var addFun: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_add_activity)
        val intentPhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val b: Bundle? = intent.extras

        var msg: String? = "Title"
        if (b != null) {
            msg = b.getString("msg")
            displayTitle = findViewById(R.id.display_title_map_add)
            displayTitle.text = msg
        }
        cancelButton = findViewById(R.id.cancel_button)
        saveButton = findViewById(R.id.save_button)
        addFun = findViewById(R.id.add_photo_view)
        cancelButton.setOnClickListener {
            onBackPressed()
        }
        saveButton.setOnClickListener {
            onBackPressed()
        }
        addFun.setOnClickListener {
            startActivityForResult(intentPhoto, 1)
        }
    }
}