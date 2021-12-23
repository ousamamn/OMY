package com.example.omy.reviews

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.omy.MainRepository
import com.example.omy.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReviewsRepository(application: Application) {
    private var reviewsDBDao: ReviewDao? = null
    private val databaseObj:OMYDatabase? = MainRepository(application).databaseObj

    init {
        if (databaseObj != null) {
            reviewsDBDao = databaseObj.ReviewDao()
        }
    }

    companion object {
        private val scope = CoroutineScope(Dispatchers.IO)
        private class InsertAsyncTask(private val dao: ReviewDao?) : ViewModel() {
            suspend fun insertInBackground(vararg params: Review) {
                scope.launch {
                    for(param in params) {
                        val insertedId = this@InsertAsyncTask.dao?.insert(param)
                    }
                }
            }
        }
    }

    /**
     * Get all the existing reviews
     *
     * @return all reviews
     */
    fun getReviews(): LiveData<List<Review?>>? {
        return reviewsDBDao?.getAll()
    }

    /**
     * Creates a new review, saving it to the database
     *
     * @param review A review to be saved
     * @return void
     */
    suspend fun createNewReview(review: Review) {
        // somehow create a new review
        InsertAsyncTask(reviewsDBDao).insertInBackground(review)
    }
}