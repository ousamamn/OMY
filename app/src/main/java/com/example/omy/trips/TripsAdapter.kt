package com.example.omy.trips

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.omy.R

class TripsAdapter : RecyclerView.Adapter<TripsAdapter.ViewHolder> {
    private lateinit var context: Context
    private var items: Array<TripElement>

    constructor(items: Array<TripElement>) {
        this.items = items
    }

    constructor(cont: Context, items: Array<TripElement>) : super() {
        this.items = items
        context = cont
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Inflate the layout, initialize the View Holder
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.trip_list_item,
            parent, false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = items[position].title
        holder.date.text = items[position].date
        holder.distance.text = items[position].distance
        holder.numOfLocations.text = items[position].numOfLocations
        holder.imageView.setImageResource(items[position].image)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    public class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var image: Image
        var title: TextView = itemView.findViewById<View>(R.id.title) as TextView
        var date: TextView = itemView.findViewById<View>(R.id.date) as TextView
        var distance: TextView = itemView.findViewById<View>(R.id.distance) as TextView
        var numOfLocations: TextView = itemView.findViewById(R.id.numOfLocations) as TextView
        var imageView: ImageView = itemView.findViewById<View>(R.id.image_item) as ImageView

    }
}