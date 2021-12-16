package com.example.omy.data
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(image:Image): Long

    @Update
    fun update(imageData:Image)

    @Delete
    fun delete(image: Image)

    @Query("SELECT * from image ORDER by id ASC")
    fun getAll(): LiveData<List<Image>>?

    @Query("SELECT * from image WHERE id=:id")
    fun getImage(id:Int):LiveData<Image>?
}
