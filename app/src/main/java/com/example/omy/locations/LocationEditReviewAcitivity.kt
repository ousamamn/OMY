package com.example.omy.locations

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.omy.R
import com.example.omy.maps.MapsCreatedActivity
import com.google.android.material.snackbar.Snackbar
import java.lang.NumberFormatException
import kotlin.math.max

class LocationEditReviewAcitivity : AppCompatActivity() {

    private lateinit var cancelButton: Button
    private lateinit var sendButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_edit_review_acitivity_activity)
        val ratingEditText = findViewById<EditText>(R.id.rating_edit_text)
        val titleEditText = findViewById<EditText>(R.id.title_edit_text)
        val descriptionEditText = findViewById<EditText>(R.id.description_name_edit_text)
        cancelButton = findViewById(R.id.cancel_button)
        sendButton = findViewById(R.id.send_button)
        ratingEditText.filters = arrayOf<InputFilter>(MinMaxFilter(1,5))
        cancelButton.setOnClickListener {
            onBackPressed()
            finish()
        }
        sendButton.setOnClickListener {
            if (TextUtils.isEmpty(titleEditText.text.toString())) {
                Toast.makeText(
                    applicationContext,
                    "Please Insert The Title",
                    Toast.LENGTH_SHORT)
                    .show();
            } else if (TextUtils.isEmpty(ratingEditText.text.toString())) {
                Toast.makeText(
                    applicationContext,
                    "Please Insert The Ratings",
                    Toast.LENGTH_SHORT)
                    .show();
            } else if (TextUtils.isEmpty(descriptionEditText.text.toString())) {
                Toast.makeText(
                    applicationContext,
                    "Please Insert The Description",
                    Toast.LENGTH_SHORT)
                    .show();
            } else {
                onBackPressed()
                finish()
                // the onBackPressed need to be change to be able to save the reviews
                //val intent = Intent(context, MapsCreatedActivity::class.java)
                //val msg = tnEditText.text.toString()
                //intent.putExtra("msg", msg)
                //context?.startActivity(intent)
            }
        }
    }

    inner class MinMaxFilter() : InputFilter {
        private var intMin: Int = 0
        private var intMax: Int = 0

        constructor(minValue: Int, maxValue: Int) : this() {
            this.intMin = minValue
            this.intMax = maxValue
        }

        override fun filter(
            source: CharSequence?,
            start: Int,
            end: Int,
            dest: Spanned?,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            try {
                val input = Integer.parseInt(dest.toString() + source.toString())
                if (isInRange(intMin, intMax, input)){
                    return null
                }
            } catch (e: NumberFormatException){
                e.printStackTrace()
            }
            return ""
        }

        private fun isInRange(a: Int, b: Int, c: Int): Boolean {
            return if (b > a) c in a..b else c in b..a
        }
    }
}



