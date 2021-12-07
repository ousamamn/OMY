package com.example.omy.data
import androidx.room.*
@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location:Location)

    @Update
    suspend fun update(locationData:Location)

    @Delete
    suspend fun delete(location: Location)

    @Query("SELECT * from location")
    suspend fun getAll():List<Location>

    @Query("SELECT * from location WHERE id=:id")
    fun getLocation(id:Int):Location
}