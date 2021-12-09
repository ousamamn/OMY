package com.example.omy.data
import androidx.room.*

@Entity(tableName = "image_location")
data class ImageLocation(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val locationId: Int,
    val imageId: Int
)
