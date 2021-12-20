package com.example.omy.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.omy.R
import com.example.omy.trips.*
import com.example.omy.data.Trip

class TripsFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var tripsSortSpinner: Spinner
    private var tripsDataset: List<Trip?> = ArrayList<Trip?>()
    private var sortedTripsDataset: List<Trip?> = ArrayList<Trip?>()


    private var tripsViewModel: TripsViewModel? = null
    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trips, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*  Locations Sort Functionality */
        tripsSortSpinner = view.findViewById(R.id.trips_filters_spinner)
        tripsSortSpinner.onItemSelectedListener = this
        ArrayAdapter.createFromResource(requireContext(), R.array.sort_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            tripsSortSpinner.adapter = adapter
        }

        //val trip1 = Trip(id = "test", tripTitle = "Me at the Zoo", tripDate = "12 Dec 2021", tripDistance = 3.2, tripWeather = "19.0", tripDescription = "description", tripListCoords = "test")
        tripsViewModel = ViewModelProvider(this)[TripsViewModel::class.java]
        //tripsDataset.clear()
        //val location = Location(id=55,locationTitle = "title",locationDescription = "description",locationLatitude = 1.2,locationLongitude = 1.1,locationTripId = 34)

        /*  Get list of trips */
        mRecyclerView = view.findViewById<RecyclerView>(R.id.trips_list)
        mLayoutManager = LinearLayoutManager(requireContext())
        mRecyclerView.layoutManager = mLayoutManager
        mAdapter = TripsAdapter(tripsDataset) as RecyclerView.Adapter<RecyclerView.ViewHolder>
        initData()
        mRecyclerView.adapter = mAdapter

        if (tripsDataset.isNotEmpty()) {
            Log.i("TAG", tripsDataset[0]!!.tripTitle!!)
            TripsAdapter(tripsDataset)
            Log.i("another", TripsAdapter.items[0]!!.tripTitle!!)
        }
    }

    private fun initData() {
        this.tripsViewModel!!.getTripsToDisplay()!!.observe(viewLifecycleOwner, { newValue ->
            tripsDataset = newValue
            mAdapter.notifyDataSetChanged()
            //mAdapter = TripsAdapter(newValue) as RecyclerView.Adapter<RecyclerView.ViewHolder>
        })
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            0 -> {
                Log.e("p0","clicked a-z")
                sortedTripsDataset =
                    tripsDataset.sortedBy { it!!.tripTitle } as MutableList<Trip>
            }
            1 -> {
                Log.e("p0","clicked z-a")
                sortedTripsDataset =
                    tripsDataset.sortedByDescending { it!!.tripTitle } as MutableList<Trip>
            }
            2 -> {
                Log.e("p0","clicked newest")
                sortedTripsDataset =
                    tripsDataset.sortedBy { it!!.tripDate } as MutableList<Trip>
            }
            3 -> {
                Log.e("p0","clicked oldest")
                sortedTripsDataset =
                    tripsDataset.sortedByDescending { it!!.tripDate } as MutableList<Trip>
            }
            else -> {
                //set the list of locations to 0, A-Z
                sortedTripsDataset = tripsDataset
            }
        }
        mAdapter = TripsAdapter(sortedTripsDataset) as RecyclerView.Adapter<RecyclerView.ViewHolder>
        mRecyclerView.adapter = mAdapter
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        mAdapter = TripsAdapter(tripsDataset) as RecyclerView.Adapter<RecyclerView.ViewHolder>
        mRecyclerView.adapter = mAdapter
    }
}