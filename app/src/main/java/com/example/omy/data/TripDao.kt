package com.example.omy.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface TripDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(trip:Trip)

    @Update
    fun update(tripData:Trip)

    @Delete
    fun delete(trip: Trip)

    @Query("SELECT * from trip")
    fun getAll(): LiveData<Trip>

    @Query("SELECT * from trip WHERE id=:id")
    fun getTrip(id:Int):Trip
}