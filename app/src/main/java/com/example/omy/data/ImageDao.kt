package com.example.omy.data
import androidx.room.*

@Dao
interface ImageDao {
    @Insert
    suspend fun insert(image:Image)

    @Update
    suspend fun update(imageData:Image)

    @Delete
    suspend fun delete(image: Image)

    @Query("SELECT * from image")
    suspend fun getAll():List<Image>

    @Query("SELECT * from image WHERE id=:id")
    fun getImage(id:Int):Image
}
