package com.example.omy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TripsActivity : AppCompatActivity() {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private val tripDataset: Array<TripElement> = arrayOf<TripElement>(
        TripElement(
            R.drawable.joe1, "Hello",
            "Would like to say hello 1"
        ),
        TripElement(
            R.drawable.joe2, "Hello",
            "Would like to say hello 2"
        ),
        TripElement(
            R.drawable.joe3, "Good Evening",
            "JWould like to say hello 3"
        ),
        TripElement(
            R.drawable.joe1, "Hello",
            "Would like to say hello 4"
        ),
        TripElement(
            R.drawable.joe2, "Hello",
            "Would like to say hello 5"
        ),
        TripElement(
            R.drawable.joe3, "Good Evening",
            "JWould like to say hello 6"
        ),
        TripElement(
            R.drawable.joe1, "Hello",
            "Would like to say hello 1"
        ),
        TripElement(
            R.drawable.joe2, "Hello",
            "Would like to say hello 2"
        ),
        TripElement(
            R.drawable.joe3, "Good Evening",
            "JWould like to say hello 3"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trips_activity)

        mRecyclerView = findViewById<RecyclerView>(R.id.trips_list)

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = LinearLayoutManager(this)
        mRecyclerView.layoutManager = mLayoutManager

        // specify an adapter (see also next example)
        mAdapter = MyAdapter(tripDataset) as RecyclerView.Adapter<RecyclerView.ViewHolder>
        mRecyclerView.adapter = mAdapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }
}