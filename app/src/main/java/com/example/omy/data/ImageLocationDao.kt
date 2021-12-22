package com.example.omy.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageLocationDao {
        @Query("SELECT * FROM location")
        fun getLocationWithImages(): LiveData<List<LocationWithImages>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(imageLocation:ImageLocation)
    }