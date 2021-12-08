package com.example.omy.data
import androidx.room.*
@Entity(tableName = "image")
data class Image(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    @ColumnInfo(name="path") val imagePath: String?,
)
