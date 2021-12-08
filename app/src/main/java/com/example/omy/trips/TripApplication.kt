package com.example.omy.trips

import android.app.Application
import com.example.omy.data.OMYDatabase

class TripApplication: Application() {
    val databaseObj: OMYDatabase by lazy { OMYDatabase.getDatabase(this) }
}