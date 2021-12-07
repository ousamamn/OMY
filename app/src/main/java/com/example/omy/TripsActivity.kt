package com.example.omy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.omy.data.Location
import com.example.omy.data.OMYDatabase
import com.example.omy.data.Trip
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class TripsActivity : AppCompatActivity() {


    val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    val databaseObj: OMYDatabase by lazy { OMYDatabase.getDatabase(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trips)

        val trip1 = Trip (id = 1,tripTitle = "title",tripDescription = "description",tripDistance = 2.5,tripDate = "date",tripWeather = 19)
        val location = Location(id=55,locationTitle = "title",locationDescription = "description",locationLatitude = 1.2,locationLongitude = 1.1,locationTripId = 34)

        scope.launch(Dispatchers.IO) {
            async { databaseObj.TripDao().insert(trip1) }
        }
        scope.launch(Dispatchers.IO) {
            async { databaseObj.LocationDao().insert(location) }
        }

    }
}