package com.example.omy.data
import androidx.room.*
@Entity(tableName = "review",
    foreignKeys = [(ForeignKey(entity = Location::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("location_id"),
        onDelete = ForeignKey.CASCADE))])
data class Review(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    @ColumnInfo(name="title") val reviewTitle: String?,
    @ColumnInfo(name="description") val reviewDescription: String?,
    @ColumnInfo(name="location_id") val locationId: Int
)
