package com.example.omy.locations

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.omy.R

class LocationsAdapter : RecyclerView.Adapter<LocationsAdapter.ViewHolder> {
    private lateinit var context: Context
    private var items: Array<LocationElement>

    constructor(items: Array<LocationElement>) {
        this.items = items
    }

    constructor(cont: Context, items: Array<LocationElement>) : super() {
        this.items = items
        context = cont
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Inflate the layout, initialize the View Holder
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.location_list_item,
            parent, false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleLocation.text = items[position].titleLocation
        holder.longitude.text = items[position].longitude
        holder.latitude.text = items[position].latitude
        holder.numOfReviews.text = items[position].numOfReviews
        holder.numOfPhotos.text = items[position].numOfPhotos
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleLocation: TextView = itemView.findViewById<View>(R.id.location_title) as TextView
        var longitude: TextView = itemView.findViewById<View>(R.id.longitude) as TextView
        var latitude: TextView = itemView.findViewById<View>(R.id.latitude) as TextView
        var numOfReviews: TextView = itemView.findViewById<View>(R.id.num_of_reviews) as TextView
        var numOfPhotos: TextView = itemView.findViewById<View>(R.id.num_of_photos) as TextView

    }
}