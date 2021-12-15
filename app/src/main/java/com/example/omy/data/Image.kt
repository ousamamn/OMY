package com.example.omy.data
import androidx.room.*
import pl.aprilapps.easyphotopicker.MediaFile

@Entity(tableName = "image")
data class Image(
    @PrimaryKey(autoGenerate = true) var id: Int=0,
    @ColumnInfo(name="path") var imagePath: String?,
    @ColumnInfo(name="file") var fileValid: Int = -1
)
