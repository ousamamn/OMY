package com.example.omy.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ReviewDao {
    @Insert
    fun insert(review:Review)

    @Update
    fun update(reviewData:Review)

    @Delete
    fun delete(review: Review)

    @Query("SELECT * from review")
    fun getAll(): LiveData<List<Review?>>?

    @Query("SELECT * from review WHERE id=:id")
    fun getReview(id:Int):Review
}