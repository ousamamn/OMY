package com.example.omy.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.omy.R
import com.example.omy.trips.*
import com.example.omy.data.Trip
import com.example.omy.data.TripDao
import com.example.omy.databinding.FragmentTripsBinding
import com.example.omy.databinding.TripsActivityBinding
import kotlinx.coroutines.*

class TripsFragment : Fragment() {
    lateinit var tripsFilterSpinner: Spinner
    lateinit var tripsDaoObj: TripDao
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    //private var tripsDataset: LiveData<List<Trip>>
    private var myDataset: List<Trip> = ArrayList<Trip>()
    private var tripsViewModel: TripsViewModel? = null
    //private var tripsAdapter : TripsAdapter()
    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    private var myViewModel: TripsViewModel? = null
    private val newDataSet : ArrayList<Trip> = ArrayList<Trip>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trips, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val trip1 = Trip(id = 0, tripTitle = "Me at the Zoo new", tripDate = "12 Dec 2021",
            tripDistance = 3.2, tripWeather = 19, tripDescription = "description", tripLocations = 3)
        //val location = Location(id=55,locationTitle = "title",locationDescription = "description",locationLatitude = 1.2,locationLongitude = 1.1,locationTripId = 34)
        this.tripsViewModel = ViewModelProvider(this)[TripsViewModel::class.java]
        /*  Trips Filter Functionality */
        tripsFilterSpinner = view.findViewById(R.id.trips_filters_spinner)
        ArrayAdapter.createFromResource(requireContext(), R.array.trips_filter_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            tripsFilterSpinner.adapter = adapter
        }
        //tripsViewModel?.createNewTrip(trip1)


        /*  Get list of trips */


        mRecyclerView = view.findViewById<RecyclerView>(R.id.trips_list)
        mLayoutManager = LinearLayoutManager(requireContext())
        mRecyclerView.layoutManager = mLayoutManager
        tripsViewModel!!.getTripsToDisplay()?.observe(viewLifecycleOwner, { newValue ->
            myDataset = newValue
            mAdapter = TripsAdapter(myDataset) as RecyclerView.Adapter<RecyclerView.ViewHolder>
            mRecyclerView.adapter = mAdapter
            //newDataSet.addAll(myDataset)

        }
        )
        if (myDataset.isNotEmpty()){
            newDataSet.add((myDataset[1]))
        }



        //mAdapter = TripsAdapter() as RecyclerView.Adapter<RecyclerView.ViewHolder>
        //mAdapter = TripsAdapter(myDataset) as RecyclerView.Adapter<RecyclerView.ViewHolder>
       // mRecyclerView.adapter = mAdapter



    }
}