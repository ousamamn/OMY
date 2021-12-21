package com.example.omy.maps

import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.util.Log
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.text.DateFormat
import java.util.*

/*
 * Implementation of service
 */
class LocationService : Service {
    private var mCurrentLocation: Location? = null
    private var mLastUpdateTime: String? = null

    private var startMode: Int = 0             // indicates how to behave if the service is killed
    private var binder: IBinder? = null        // interface for clients that bind
    private var allowRebind: Boolean = false   // indicates whether onRebind should be used

    constructor(name: String?) : super()
    constructor() : super()

    override fun onCreate() {
        Log.e("Location Service", "onCreate finished")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (LocationResult.hasResult(intent!!)) {
            val locResults = LocationResult.extractResult(intent)
            for (location in locResults.locations) {
                if (location == null) continue
                Log.i("This is in service, New Location", "Current location: $location")
                mCurrentLocation = location
                mLastUpdateTime = DateFormat.getTimeInstance().format(Date())
                Log.i("This is in service, MAP", "new location " + mCurrentLocation.toString())
                if (MapCreatedActivity.getActivity() != null)
                    MapCreatedActivity.getActivity()?.runOnUiThread(Runnable {
                        try {
                            MapCreatedActivity.visitedLongLatLocations.add(Pair(mCurrentLocation!!.latitude,mCurrentLocation!!.longitude))
                            MapCreatedActivity.getMap().clear()
                            MapCreatedActivity.getMap().addMarker(
                            MarkerOptions().position(
                                LatLng(
                                    mCurrentLocation!!.latitude,
                                    mCurrentLocation!!.longitude
                                )
                            )
                                .title(mLastUpdateTime)
                            )
                            val zoom = CameraUpdateFactory.zoomTo(16f)
                            MapCreatedActivity.getMap().moveCamera(
                                CameraUpdateFactory.newLatLng(
                                    LatLng(
                                        mCurrentLocation!!.latitude,
                                        mCurrentLocation!!.longitude
                                    )
                                )
                            )

                            MapCreatedActivity.getMap().animateCamera(zoom)
                        } catch (e: Exception) {
                            Log.e("LocationService", "Error cannot write on map " + e.message)
                        }
                    })
            }
        }
        return startMode
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onUnbind(intent: Intent): Boolean {
        return allowRebind
    }

    override fun onRebind(intent: Intent) {
    }

    override fun onDestroy() {
        Log.e("Service", "end")
    }
}