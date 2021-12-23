package com.example.omy.reviews

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.example.omy.data.Review


class ReviewsViewModel(application: Application): AndroidViewModel(application) {
    private var reviewsRepository: ReviewsRepository = ReviewsRepository(application)
    private var reviewsToDisplay: LiveData<List<Review?>>? = null

    init {
        this.reviewsToDisplay = this.reviewsRepository.getReviews()
    }

    /**
     * Fetch specific reviews
     *
     * @return List of reviews data
     */
    fun getReviewsToDisplay(): LiveData<List<Review?>>? {
        if (this.reviewsToDisplay == null) {
            this.reviewsToDisplay = MutableLiveData()
        }
        return this.reviewsToDisplay
    }

    /**
     * Launches a new review and creates it in the database
     *
     * @param review A review
     * @return void
     */
    fun createNewReview(review: Review) {
        viewModelScope.launch(Dispatchers.IO) { reviewsRepository.createNewReview(review) }
        }
    }