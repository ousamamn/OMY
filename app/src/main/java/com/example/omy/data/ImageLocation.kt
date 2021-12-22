package com.example.omy.data
import androidx.room.*

@Entity(tableName = "image_location", primaryKeys = ["locationId","imageId"])
data class ImageLocation(
    val locationId: Long,
    val imageId: Long
)
