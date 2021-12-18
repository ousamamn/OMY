package com.example.omy.data
import androidx.room.*
import java.util.*

@Entity(tableName = "trip")
data class Trip(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    @ColumnInfo(name="title") val tripTitle: String?,
    @ColumnInfo(name="date") val tripDate: String?,
    @ColumnInfo(name="distance") val tripDistance: Double,
    @ColumnInfo(name="weather") val tripWeather: String?,
    @ColumnInfo(name="description") val tripDescription: String?,
)
