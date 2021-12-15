package com.example.omy.photos

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.example.omy.R

class PhotoShowActivity : FragmentActivity() {
    private lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_activity)

        val b: Bundle? = intent.extras
        var position = -1

        if (b != null) {
            // this is the image position in the itemList
            position = b.getInt("position")
            if (position != -1) {
                val imageView = findViewById<ImageView>(R.id.image)
                val element = PhotosAdapter.items[position]
                if (element.fileValid != -1) {
                    imageView.setImageResource(element.fileValid)
                } else if (element.imagePath != null) {
                    val myBitmap = BitmapFactory.decodeFile(element.imagePath)
                    imageView.setImageBitmap(myBitmap)
                }
            }
        }
        backButton = findViewById(R.id.back_to_previous)
        backButton.setOnClickListener {
            onBackPressed()
            finish()
        }
    }
}