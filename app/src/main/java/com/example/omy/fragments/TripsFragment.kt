package com.example.omy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.omy.R
import com.example.omy.trips.*
import com.example.omy.data.Trip
import com.example.omy.data.TripDao
import kotlinx.coroutines.*

class TripsFragment : Fragment() {
    lateinit var tripsFilterSpinner: Spinner
    lateinit var tripsDaoObj: TripDao
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    //var tripsDataset: MutableList<Trip> = ArrayList()

    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    private val tripsDataset: Array<TripElement> = arrayOf<TripElement>(
        TripElement(
            "Me at the Zoo", "3 Oct 2021", "3.2",
            "4"
        ),
        TripElement(
            "Morning Hike", "12 Sep 2021", "7",
            "3"
        ),
        TripElement(
            "Picnic in a Park", "31 Aug 2021", "0.4",
            "11"
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trips, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val trip1 = Trip(id = 1, tripTitle = "Me at the Zoo", tripDate = "12 Dec 2021",
            tripDistance = 3.2, tripWeather = 19, tripDescription = "description", tripLocations = 3)
        //val location = Location(id=55,locationTitle = "title",locationDescription = "description",locationLatitude = 1.2,locationLongitude = 1.1,locationTripId = 34)

        scope.launch(Dispatchers.IO) {
            async {  }
        }
        //mRecyclerView.adapter = mAdapter
        //mAdapter = TripsAdapter(tripsDataset) as RecyclerView.Adapter<RecyclerView.ViewHolder>
        //recyclerView.adapter = adapter

        //scope.launch(Dispatchers.IO) {
        //async { databaseObj.LocationDao().insert(location) }
        //}

        /*  Trips Filter Functionality */
        tripsFilterSpinner = view.findViewById(R.id.trips_filters_spinner)
        ArrayAdapter.createFromResource(requireContext(), R.array.trips_filter_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            tripsFilterSpinner.adapter = adapter
        }

        /*  Get list of trips */
        mRecyclerView = view.findViewById<RecyclerView>(R.id.trips_list)
        mLayoutManager = LinearLayoutManager(requireContext())
        mRecyclerView.layoutManager = mLayoutManager

        //mAdapter = TripsAdapter(tripsDataset) as RecyclerView.Adapter<RecyclerView.ViewHolder>
        //mRecyclerView.adapter = mAdapter

    }
}