package com.example.omy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TripsActivity : AppCompatActivity() {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trips_activity)

        mRecyclerView = findViewById<RecyclerView>(R.id.trips_list)
        mLayoutManager = LinearLayoutManager(this)
        mRecyclerView.layoutManager = mLayoutManager

        mAdapter = TripsAdapter(tripDataset) as RecyclerView.Adapter<RecyclerView.ViewHolder>
        mRecyclerView.adapter = mAdapter

    }
}