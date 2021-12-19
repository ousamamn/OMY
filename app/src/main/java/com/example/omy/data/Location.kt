package com.example.omy.data
import androidx.room.*

@Entity(tableName = "location",indices = [Index(value = ["longitude","latitude"])],
    foreignKeys = [(ForeignKey(entity = Trip::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("trip_id"),
        onDelete = ForeignKey.CASCADE))])
data class Location(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    @ColumnInfo(name="title") val locationTitle: String?,
    @ColumnInfo(name="description") val locationDescription: String?,
    @ColumnInfo(name="longitude") val locationLongitude: Double,
    @ColumnInfo(name="latitude") val locationLatitude: Double,
    @ColumnInfo(name="trip_id") var locationTripId: String?

)
