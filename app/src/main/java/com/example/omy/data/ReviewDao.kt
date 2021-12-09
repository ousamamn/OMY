package com.example.omy.data

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
    fun getAll():List<Review>

    @Query("SELECT * from review WHERE id=:id")
    fun getReview(id:Int):Review
}