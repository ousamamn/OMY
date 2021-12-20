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
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.omy.R
import com.example.omy.data.Location
import com.example.omy.locations.*
import com.example.omy.trips.TripsAdapter
import java.util.ArrayList

class LocationsFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var locationsSortSpinner: Spinner
    private var locationsDataset: List<Location?> = ArrayList<Location?>()
    private var sortedLocationsDataset: List<Location?> = ArrayList<Location?>()

    private var locationsViewModel: LocationsViewModel? = null
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var recyclerEmpty: TextView
    private lateinit var mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_locations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerEmpty = view.findViewById(R.id.no_locations)

        /*  Locations Sort Functionality */
        locationsSortSpinner = view.findViewById(R.id.locations_filters_spinner)
        locationsSortSpinner.onItemSelectedListener = this
        ArrayAdapter.createFromResource(requireContext(), R.array.sort_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            locationsSortSpinner.adapter = adapter
        }

        locationsViewModel = ViewModelProvider(this)[LocationsViewModel::class.java]

        /*  Get list of locations */
        mRecyclerView = view.findViewById<RecyclerView>(R.id.locations_list)
        mLayoutManager = LinearLayoutManager(requireContext())
        mRecyclerView.layoutManager = mLayoutManager
        mAdapter = LocationsAdapter(locationsDataset) as RecyclerView.Adapter<RecyclerView.ViewHolder>
        initData()
        mRecyclerView.adapter = mAdapter

        if (locationsDataset.isNotEmpty()) {
            Log.i("TAG", locationsDataset[0]!!.locationTitle!!)
            LocationsAdapter(locationsDataset)
            Log.i("another", TripsAdapter.items[0]!!.tripTitle!!)
        }
    }

    private fun initData() {
        this.locationsViewModel!!.getLocationsToDisplay()!!.observe(viewLifecycleOwner, { newValue ->
            locationsDataset = newValue
            mAdapter.notifyDataSetChanged()
            if (newValue.isEmpty()) recyclerEmpty.visibility = View.VISIBLE
            else recyclerEmpty.visibility = View.GONE
        })
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(locationsDataset.isNotEmpty()) {
            when (position) {
                0 -> {
                    Log.e("p0","clicked a-z")
                    sortedLocationsDataset =
                        locationsDataset.sortedBy { it!!.locationTitle } as MutableList<Location>
                } 1 -> {
                    Log.e("p0","clicked z-a")
                    sortedLocationsDataset =
                        locationsDataset.sortedByDescending { it!!.locationTitle } as MutableList<Location>
                } 2 -> {
                    Log.e("p0","clicked newest")
                    sortedLocationsDataset =
                        locationsDataset.sortedByDescending { it!!.locationLatitude } as MutableList<Location>
                } 3 -> {
                    Log.e("p0","clicked oldest")
                    sortedLocationsDataset =
                        locationsDataset.sortedBy { it!!.locationLatitude } as MutableList<Location>
                } else -> {
                    //set the list of locations to 0, A-Z
                    sortedLocationsDataset = locationsDataset
                }
            }
        }
        mAdapter = LocationsAdapter(sortedLocationsDataset) as RecyclerView.Adapter<RecyclerView.ViewHolder>
        mRecyclerView.adapter = mAdapter
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        mAdapter = LocationsAdapter(locationsDataset) as RecyclerView.Adapter<RecyclerView.ViewHolder>
        mRecyclerView.adapter = mAdapter
    }
}