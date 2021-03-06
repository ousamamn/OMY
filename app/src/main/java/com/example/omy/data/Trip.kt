package com.example.omy.data
import androidx.annotation.NonNull
import androidx.room.*
import java.util.*

@Entity(tableName = "trip")
data class Trip(
    @NonNull @PrimaryKey val id: String,
    @ColumnInfo(name="title") val tripTitle: String?,
    @ColumnInfo(name="date") val tripDate: String?,
    @ColumnInfo(name="distance") val tripDistance: Double,
    @ColumnInfo(name="weather") val tripWeather: String?,
    @ColumnInfo(name="coordinates") val tripListCoords: String?,
)
