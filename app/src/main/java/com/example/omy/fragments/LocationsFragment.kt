package com.example.omy.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.omy.R
import com.example.omy.data.Location
import com.example.omy.locations.LocationsAdapter
import com.example.omy.locations.LocationsViewModel
import java.util.*

class LocationsFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var locationsSortSpinner: Spinner
    private var locationsDataset: List<Location?> = ArrayList<Location?>()
    private var sortedLocationsDataset: List<Location?> = ArrayList<Location?>()
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var recyclerEmpty: TextView
    private lateinit var mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    private var locationsViewModel: LocationsViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_locations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerEmpty = view.findViewById(R.id.no_locations)

        //  Locations Sort Functionality
        locationsSortSpinner = view.findViewById(R.id.locations_filters_spinner)
        locationsSortSpinner.onItemSelectedListener = this
        ArrayAdapter.createFromResource(requireContext(), R.array.sort_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            locationsSortSpinner.adapter = adapter
        }

        locationsViewModel = ViewModelProvider(this)[LocationsViewModel::class.java]

        //  Get list of locations
        mRecyclerView = view.findViewById(R.id.locations_list)
        mLayoutManager = LinearLayoutManager(requireContext())
        mRecyclerView.layoutManager = mLayoutManager
        mAdapter = LocationsAdapter(locationsDataset) as RecyclerView.Adapter<RecyclerView.ViewHolder>
        initData()
        if (locationsDataset.isNotEmpty()) {
            LocationsAdapter(locationsDataset)
        }
    }

    /**
     * Initialize the locations data from database
     *
     * @return List of locations data
     */
    private fun initData() {
        this.locationsViewModel!!.getLocationsToDisplay()!!.observe(viewLifecycleOwner, { newValue ->
            locationsDataset = newValue
            mAdapter = LocationsAdapter(locationsDataset) as RecyclerView.Adapter<RecyclerView.ViewHolder>
            mAdapter.notifyDataSetChanged()
            if (newValue.isEmpty()) recyclerEmpty.visibility = View.VISIBLE
            else recyclerEmpty.visibility = View.GONE

            mRecyclerView.adapter = mAdapter
        })
    }

    /**
     * Select a specific item from a list
     *
     * @param parent A AdapterView for filter item
     * @param view Filter view box
     * @param position filter item's position
     * @param id location's id
     * @return void
     */
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(locationsDataset.isNotEmpty()) {
            when (position) {
                0 -> {
                    // Sort trips A - Z
                    sortedLocationsDataset =
                        locationsDataset.sortedBy { it!!.locationTitle } as MutableList<Location>
                } 1 -> {
                    // Sort trips Z - A
                    sortedLocationsDataset =
                        locationsDataset.sortedByDescending { it!!.locationTitle } as MutableList<Location>
                } 2 -> {
                    // Sort by date, from the newest
                    sortedLocationsDataset =
                        locationsDataset.sortedByDescending { it!!.locationLatitude } as MutableList<Location>
                } 3 -> {
                    // Sort by date, from oldest
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

    /**
     * Select for nothing
     *
     * @param p0 A AdapterView for nothing selected
     * @return void
     */
    override fun onNothingSelected(p0: AdapterView<*>?) {
        mAdapter = LocationsAdapter(locationsDataset) as RecyclerView.Adapter<RecyclerView.ViewHolder>
        mRecyclerView.adapter = mAdapter
    }
}