package com.example.omy.locations

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.omy.R
import java.lang.NumberFormatException

class LocationEditReviewAcitivity : AppCompatActivity() {

    private lateinit var cancelButton: Button
    private lateinit var sendButton: Button
    private lateinit var displayHeading: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_edit_review_acitivity)
        val b: Bundle? = intent.extras

        var msg: String? = "Title"
        if (b != null) {
            msg = b.getString("msg")
            displayHeading = findViewById(R.id.review_heading)
            displayHeading.text = "REVIEWS - $msg"
        }

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
                    "Please insert the title",
                    Toast.LENGTH_SHORT)
                    .show();
            } else if (TextUtils.isEmpty(ratingEditText.text.toString())) {
                Toast.makeText(
                    applicationContext,
                    "Please insert the ratings",
                    Toast.LENGTH_SHORT)
                    .show();
            } else if (TextUtils.isEmpty(descriptionEditText.text.toString())) {
                Toast.makeText(
                    applicationContext,
                    "Please insert the description",
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
        setupUI(findViewById(R.id.on_touch_review))
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

    private fun hideSoftKeyboard(locationEditReviewAcitivity: LocationEditReviewAcitivity) {
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
    }
}



