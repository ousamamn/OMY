package com.example.omy.data
import androidx.room.*

@Dao
interface ImageDao {
    @Insert
    fun insert(image:Image)

    @Update
    fun update(imageData:Image)

    @Delete
    fun delete(image: Image)

    @Query("SELECT * from image")
    fun getAll():List<Image>

    @Query("SELECT * from image WHERE id=:id")
    fun getImage(id:Int):Image
}
