package com.example.omy.trips


import android.content.Context
import android.content.Intent
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.omy.R
import com.example.omy.data.Trip
import com.example.omy.photos.PhotosAdapter
import org.w3c.dom.Text

class TripsAdapter : RecyclerView.Adapter<TripsAdapter.ViewHolder> {
    private lateinit var context: Context
  
    constructor() {
        items = ArrayList<Trip>()
    }
    constructor(items: List<Trip>): super() {
        TripsAdapter.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Inflate the layout, initialize the View Holder
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.trip_list_item,
            parent, false
        )
        val holder: ViewHolder = ViewHolder(v)
        context = parent.context
        return holder
    }

    fun updateTripList(tripList: List<Trip>) {
        //this.tripList
        items = tripList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = items[position].tripTitle
        holder.date.text = items[position].tripDate
        holder.distance.text = items[position].tripDistance.toString()
        holder.numOfLocations.text = items[position].tripLocations.toString()
        holder.itemView.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, TripShowActivity::class.java)
            intent.putExtra("position", position)
            context.startActivity(intent)
        })
    }

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
        lateinit var items: List<Trip>
    }
}