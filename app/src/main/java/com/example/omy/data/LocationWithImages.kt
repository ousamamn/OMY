package com.example.omy.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class LocationWithImages(
    @Embedded val location:Location,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        entity = Image::class,
        associateBy = Junction(
            value = ImageLocation::class,
            parentColumn = "locationId",
            entityColumn = "imageId"
        )
    ) val imageIdList: List<Image>
)

