package com.example.omy.data
import android.graphics.Bitmap
import androidx.room.*
import pl.aprilapps.easyphotopicker.MediaFile

@Entity(tableName = "image")
data class Image(
    @PrimaryKey(autoGenerate = true) var id: Int=0,
    @ColumnInfo(name="uri") val imageUri: String,
    @ColumnInfo(name="title") var imageTitle: String,
    @ColumnInfo(name="thumbnailUri") var thumbnailUri: String? = null,
    @ColumnInfo(name="date") var imageDate: String? = null,)
{
    @Ignore
    var thumbnail: Bitmap? = null
}
