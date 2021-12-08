package com.example.omy.data
import androidx.room.*
@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(location:Location)

    @Update
    fun update(locationData:Location)

    @Delete
    fun delete(location: Location)

    @Query("SELECT * from location")
    fun getAll():List<Location>

    @Query("SELECT * from location WHERE id=:id")
    fun getLocation(id:Int):Location
}