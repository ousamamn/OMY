package com.example.omy.photos

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import com.example.omy.MainActivity
import com.example.omy.R
import com.example.omy.data.ImageDao
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PhotoShowActivity : FragmentActivity() {
    private lateinit var backButton: ImageView
    lateinit var daoObj: ImageDao

    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val position = result.data?.getIntExtra("position", -1)
            val id = result.data?.getIntExtra("id", -1)
            val del_flag = result.data?.getIntExtra("deletion_flag", -1)
            var intent = Intent().putExtra("position", position)
                .putExtra("id", id).putExtra("deletion_flag", del_flag)
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    this.setResult(result.resultCode, intent)
                    this.finish()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_activity)

        val b: Bundle? = intent.extras
        var position = -1

        if (b != null) {
            // this is the image position in the itemList
            position = b.getInt("position")
            /*if (position != -1) {
                val imageView = findViewById<ImageView>(R.id.image)
                val element = PhotosAdapter.items[position]
                if (element.fileValid != -1) {
                    imageView.setImageResource(element.fileValid)
                } else if (element.imagePath != null) {
                    val myBitmap = BitmapFactory.decodeFile(element.imagePath)
                    imageView.setImageBitmap(myBitmap)
                }
            }*/
        }
        displayData(position)
        backButton = findViewById(R.id.back_to_previous)
        backButton.setOnClickListener {
            onBackPressed()
            finish()
        }

    }

    private fun displayData(position: Int){
        if (position != -1) {
            val imageView = findViewById<ImageView>(R.id.image)
            //val titleToolbar = findViewById<Toolbar>(R.id.)
            //val descriptionTextView = findViewById<TextView>(R.id.show_image_description)
            val imageData = PhotosAdapter.items[position]

            imageView.setImageBitmap(PhotosAdapter.items[position].thumbnail!!)
            //titleToolbar.title = MyAdapter.items[position].imageTitle
            //descriptionTextView.text = MyAdapter.items[position].imageDescription

           /* val fabEdit: FloatingActionButton = findViewById(R.id.fab_edit)
            fabEdit.setOnClickListener(View.OnClickListener {
                startForResult.launch(
                    Intent( this, EditActivity::class.java).apply {
                        putExtra("position", position)
                    }
                )
            })*/
        }
    }
}