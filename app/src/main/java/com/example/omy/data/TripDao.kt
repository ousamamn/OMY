package com.example.omy.data

import androidx.room.*

@Dao
interface TripDao : MutableList<TripDao> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trip:Trip)

    @Update
    suspend fun update(tripData:Trip)

    @Delete
    suspend fun delete(trip: Trip)

    @Query("SELECT * from trip")
    suspend fun getAll():MutableList<Trip>

    @Query("SELECT * from trip WHERE id=:id")
    fun getTrip(id:Int):Trip
}