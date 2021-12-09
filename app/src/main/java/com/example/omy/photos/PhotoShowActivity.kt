package com.example.omy.photos

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.example.omy.R

class PhotoShowActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_show)

        val b: Bundle? = intent.extras
        var position = -1

        if (b != null) {
            // this is the image position in the itemList
            position = b.getInt("position")
            if (position != -1) {
                val imageView = findViewById<ImageView>(R.id.image)
                val element = PhotosAdapter.items[position]
                if (element.image != -1) {
                    imageView.setImageResource(element.image)
                } else if (element.file != null) {
                    val myBitmap = BitmapFactory.decodeFile(element.file!!.file.absolutePath)
                    imageView.setImageBitmap(myBitmap)
                }
            }
        }
    }
}