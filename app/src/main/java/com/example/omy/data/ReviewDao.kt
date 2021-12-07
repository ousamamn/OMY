package com.example.omy.data

import androidx.room.*

@Dao
interface ReviewDao {
    @Insert
    suspend fun insert(review:Review)

    @Update
    suspend fun update(reviewData:Review)

    @Delete
    suspend fun delete(review: Review)

    @Query("SELECT * from review")
    suspend fun getAll():List<Review>

    @Query("SELECT * from review WHERE id=:id")
    fun getReview(id:Int):Review
}