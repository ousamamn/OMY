package com.example.omy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.omy.R
import com.example.omy.data.Image
import com.example.omy.data.Location
import com.example.omy.locations.*
import com.example.omy.photos.PhotosAdapter
import com.example.omy.photos.PhotosViewModel
import com.example.omy.trips.TripsAdapter
import com.example.omy.trips.TripsViewModel
import java.util.ArrayList

class LocationsFragment : Fragment() {
    lateinit var locationsFilterSpinner: Spinner
    lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    private var locationsViewModel: LocationsViewModel? = null
    private val locationsDataset: MutableList<Location?> = ArrayList<Location?>()
    /*private val locationDataset: Array<LocationElement> = arrayOf<LocationElement>(
        LocationElement(
            "Big monument", "-0.14", "86.67",
            "2", "11"
        ),
        LocationElement(
            "Super old tree", "85.62", "-2.90",
            "2", "11"
        ),
        LocationElement(
            "National Gallery", "33.91", "128.33",
            "2", "11"
        ),
        LocationElement(
            "National Gallery", "33.91", "128.33",
            "2", "11"
        )
    )*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_locations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*  Locations Filter Functionality */
        locationsFilterSpinner = view.findViewById(R.id.locations_filters_spinner)
        locationsViewModel = ViewModelProvider(this)[LocationsViewModel::class.java]
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.locations_filter_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            locationsFilterSpinner.adapter = adapter
        }

        /*  Get list of locations */
        mRecyclerView = view.findViewById<RecyclerView>(R.id.locations_list)
        mLayoutManager = LinearLayoutManager(requireContext())
        mRecyclerView.layoutManager = mLayoutManager
        mAdapter = LocationsAdapter(locationsDataset) as RecyclerView.Adapter<RecyclerView.ViewHolder>
        initData()

        //mAdapter = LocationsAdapter(locationDataset) as RecyclerView.Adapter<RecyclerView.ViewHolder>
        mRecyclerView.adapter = mAdapter
    }

    private fun initData() {
        this.locationsViewModel!!.getLocationsToDisplay()!!.observe(viewLifecycleOwner, { newValue ->
            //tripsDataset = newValue
            mAdapter.notifyDataSetChanged()
            mAdapter = LocationsAdapter(newValue) as RecyclerView.Adapter<RecyclerView.ViewHolder>

            //mRecyclerView.adapter = mAdapter
        })
    }
}