package com.example.omy.reviews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.omy.R
import com.example.omy.data.Review
import com.example.omy.locations.LocationShowActivity
import java.util.ArrayList

class LocationReviewsActivity : AppCompatActivity() {
    private lateinit var mRecyclerViewReviews: RecyclerView
    private lateinit var mReviewsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    private lateinit var mReviewsLayoutManager: RecyclerView.LayoutManager
    private var reviewsDataset: List<Review?> = ArrayList<Review?>()
    private var reviewsViewModel: ReviewsViewModel? = null
    private lateinit var reviewsRecyclerEmpty: TextView

    private lateinit var displayTitle: TextView
    private lateinit var backToPrevious: ImageView
    var locationId: Int? = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_reviews_activity)

        reviewsViewModel = ViewModelProvider(this)[ReviewsViewModel::class.java]

        reviewsRecyclerEmpty = findViewById(R.id.no_reviews)
        mRecyclerViewReviews = findViewById(R.id.location_reviews)
        mReviewsLayoutManager = LinearLayoutManager(this)
        mRecyclerViewReviews.layoutManager = mReviewsLayoutManager
        mReviewsAdapter = LocationReviewsAdapter(reviewsDataset) as RecyclerView.Adapter<RecyclerView.ViewHolder>
        initDataReviews()
        mReviewsAdapter.notifyDataSetChanged()
        if (reviewsDataset.isNotEmpty()) {
            LocationReviewsAdapter(reviewsDataset)
        }
        mRecyclerViewReviews.adapter = mReviewsAdapter

        val b: Bundle? = intent.extras

        var locationTitle: String? = "Title"

        if (b != null) {
            locationTitle = b.getString("locationTitle")
            locationId = b.getInt("locationPosition")
            displayTitle = findViewById(R.id.title_name)
            displayTitle.text = locationTitle
        }
        backToPrevious = findViewById(R.id.back_to_previous)
        backToPrevious.setOnClickListener {
            onBackPressed()
            finish()
        }
    }

    private fun initDataReviews() {
        /*this.reviewsViewModel!!.getReviewsToDisplay()!!.observe(this, { newValue ->
            reviewsDataset = newValue
            mReviewsAdapter = LocationReviewsAdapter(reviewsDataset) as RecyclerView.Adapter<RecyclerView.ViewHolder>
            mReviewsAdapter.notifyDataSetChanged()
            if (newValue.isEmpty()) reviewsRecyclerEmpty.visibility = View.VISIBLE
            else reviewsRecyclerEmpty.visibility = View.GONE
        })*/

        this.reviewsViewModel!!.getReviewsToDisplay()!!.observe(this, { newValue ->
            reviewsDataset =
                LocationShowActivity.getLocationReviews(locationId!!+1, newValue as MutableList<Review?>)
            mReviewsAdapter = LocationReviewsAdapter(reviewsDataset) as RecyclerView.Adapter<RecyclerView.ViewHolder>
            mReviewsAdapter.notifyDataSetChanged()
            if (newValue.isEmpty()) reviewsRecyclerEmpty.visibility = View.VISIBLE
            else reviewsRecyclerEmpty.visibility = View.GONE
            mRecyclerViewReviews.adapter = mReviewsAdapter
        })
    }
}