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

class TripsFragment : Fragment() {
    lateinit var tripsFilterSpinner: Spinner
    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    private val tripDataset: Array<TripElement> = arrayOf<TripElement>(
        TripElement(
            "Me at the Zoo", "3 Oct 2021", "3.2",
            "4", R.drawable.joe1
        ),
        TripElement(
            "Morning Hike", "12 Sep 2021", "7",
            "3", R.drawable.joe2
        ),
        TripElement(
            "Picnic in a Park", "31 Aug 2021", "0.4",
            "11", R.drawable.joe3
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

        /*  Trips Filter Functionality */
        tripsFilterSpinner = view.findViewById(R.id.trips_filters_spinner)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.trips_filter_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            tripsFilterSpinner.adapter = adapter
        }

        /*  Get list of trips */
        mRecyclerView = view.findViewById<RecyclerView>(R.id.trips_list)
        mLayoutManager = LinearLayoutManager(requireContext())
        mRecyclerView.layoutManager = mLayoutManager

        mAdapter = TripsAdapter(tripDataset) as RecyclerView.Adapter<RecyclerView.ViewHolder>
        mRecyclerView.adapter = mAdapter

    }
}