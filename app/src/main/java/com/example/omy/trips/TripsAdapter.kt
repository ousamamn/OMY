package com.example.omy.trips

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.omy.R
import com.example.omy.data.Location
import com.example.omy.data.Trip

class TripsAdapter : RecyclerView.Adapter<TripsAdapter.ViewHolder> {
    private lateinit var context: Context

    constructor(items: List<Trip?>,locations:MutableMap<String,MutableList<Location?>>): super() {
        TripsAdapter.items = items as MutableList<Trip?>
        TripsAdapter.tripAndLocation = locations
    }
    constructor(cont: Context, items: List<Trip?>) : super() {
        TripsAdapter.items = items as MutableList<Trip?>
        context = cont
    }

    /**
     * Function to update the list of trips
     *
     * @param parent A ViewGroup for trips
     * @param viewType Adapter's position
     * @return the list of trips
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Inflate the layout, initialize the View Holder
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.trip_list_item,
            parent, false)
        val holder = ViewHolder(v)
        context = parent.context
        return holder
    }

    /**
     * Function to update the list of trips
     *
     * @param tripList A list of trips
     * @return the list of trips data
     */
    fun updateTripList(tripList: MutableList<Trip?>) {
        items = tripList
    }

    /**
     * Function to add trip
     *
     * @param trip A Trip
     * @return the trip data
     */
    fun addTrip(trip: Trip){
        items.add(trip)
    }

    /**
     * Sets up the view holder for a trip
     *
     * @param holder Trip's ViewHolder
     * @param position Trip's position/id
     * @return void
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = items[position]?.tripTitle
        holder.date.text = items[position]?.tripDate
        holder.distance.text = items[position]?.tripDistance.toString()
        holder.numOfLocations.text = tripAndLocation[items[position]?.id]!!.size.toString()
        holder.itemView.setOnClickListener {
            val intent = Intent(context, TripShowActivity::class.java)
            intent.putExtra("position", position)
            intent.putExtra("numOfLocations",tripAndLocation[items[position]?.id]!!.size)
            context.startActivity(intent)
        }
    }

    /**
     * Function to get item number count
     *
     * @return the number count of item
     */
    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById<View>(R.id.trip_title) as TextView
        var date: TextView = itemView.findViewById<View>(R.id.trip_date) as TextView
        var distance: TextView = itemView.findViewById<View>(R.id.trip_distance) as TextView
        var numOfLocations: TextView = itemView.findViewById(R.id.trip_num_of_locations) as TextView
    }

    companion object {
        lateinit var items: MutableList<Trip?>
        lateinit var tripAndLocation: MutableMap<String,MutableList<Location?>>
    }
}