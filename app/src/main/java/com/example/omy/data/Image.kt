package com.example.omy.data
import android.graphics.Bitmap
import androidx.room.*
import pl.aprilapps.easyphotopicker.MediaFile

@Entity(tableName = "image")
data class Image(
    @PrimaryKey(autoGenerate = true) var id: Int=0,
    @ColumnInfo(name="uri") val imageUri: String,
    @ColumnInfo(name="title") var imageTitle: String,
    @ColumnInfo(name="description") var imageDescription: String? = null,
    @ColumnInfo(name="thumbnailUri") var thumbnailUri: String? = null,)
{
    @Ignore
    var thumbnail: Bitmap? = null
}
