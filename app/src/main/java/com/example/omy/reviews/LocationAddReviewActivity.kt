package com.example.omy.reviews

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.omy.R
import com.example.omy.data.Review
import com.example.omy.trips.TripsViewModel

class LocationAddReviewActivity : AppCompatActivity() {
    private lateinit var titleEditText: EditText
    private lateinit var ratingEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var cancelButton: Button
    private lateinit var sendButton: Button
    private lateinit var displayHeading: TextView
    private var locationTitle: String = ""
    private var locationPosition: Int = 0
    private var reviewsViewModel: ReviewsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_add_review_acitivity)

        // Get the data from the database and pass it into the activity
        val b: Bundle? = intent.extras
        reviewsViewModel = ViewModelProvider(this)[ReviewsViewModel::class.java]
        if (b != null) {
            locationTitle = b.getString("locationTitle").toString()
            locationPosition = b.getInt("locationPosition")
            Log.i("TAGGHG",locationPosition.toString())
            displayHeading = findViewById(R.id.review_heading)
            displayHeading.text = getString(R.string.add_review, locationTitle)
        }
        titleEditText = findViewById(R.id.review_title)
        ratingEditText = findViewById(R.id.review_rating)
        descriptionEditText = findViewById(R.id.review_description)
        cancelButton = findViewById(R.id.review_cancel_button)
        sendButton = findViewById(R.id.review_send_button)
        ratingEditText.filters = arrayOf<InputFilter>(MinMaxFilter(1,5))

        // Cancel the review adding activity and go back to previous page
        cancelButton.setOnClickListener {
            onBackPressed()
            finish()
        }

        // Send and save the review to database
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
                saveReviewToDB()
                val intentAllReviews = Intent(this, LocationReviewsActivity::class.java)
                val title = titleEditText.text.toString()
                intentAllReviews.putExtra("locationTitle", title)
                intentAllReviews.putExtra("locationPosition", locationPosition)
                startActivity(intentAllReviews)
                onBackPressed()
                finish()
            }
        }
        setupUI(findViewById(R.id.on_touch_review))
    }

    /**
     * Filtering and limit the number or alphabet that can be put in the text box
     *
     * @return void
     */
    inner class MinMaxFilter() : InputFilter {
        private var intMin: Int = 0
        private var intMax: Int = 0

        constructor(minValue: Int, maxValue: Int) : this() {
            this.intMin = minValue
            this.intMax = maxValue
        }

        override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {
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

    /**
     * Hide soft keyboard when not in use
     *
     * @param LocationAddReviewActivity A LocationAddReviewActivity element
     * @return void
     */
    private fun hideSoftKeyboard(locationAddReviewActivity: LocationAddReviewActivity) {
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

    /**
     * Set up the view to touch listener
     *
     * @param View A view
     * @return void
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun setupUI(view: View) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                hideSoftKeyboard(this)
                false
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until (view).childCount) {
                val innerView: View = (view).getChildAt(i)
                setupUI(innerView)
            }
        }
    }

    /**
     * Save review to database
     *
     * @return void
     */
    private fun saveReviewToDB() {
        val review = Review(reviewTitle = titleEditText.text.toString(),
            reviewDescription = descriptionEditText.text.toString(),
            reviewRating = ratingEditText.text.toString().toInt(),
            reviewLocationId = locationPosition+1)
        this.reviewsViewModel!!.createNewReview(review)
        LocationReviewsAdapter.reviews.add(review)
    }
}