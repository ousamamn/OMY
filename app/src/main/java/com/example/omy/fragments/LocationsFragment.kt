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
import com.example.omy.locations.*

class LocationsFragment : Fragment() {
    lateinit var locationsFilterSpinner: Spinner
    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    private val locationDataset: Array<LocationElement> = arrayOf<LocationElement>(
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
    )

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

        mAdapter = LocationsAdapter(locationDataset) as RecyclerView.Adapter<RecyclerView.ViewHolder>
        mRecyclerView.adapter = mAdapter
    }
}