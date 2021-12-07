package com.example.omy.locations

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.example.omy.R

class LocationsAdapter : RecyclerView.Adapter<LocationsAdapter.ViewHolder> {
    private lateinit var context: Context

    constructor(items: Array<LocationElement>) {
        LocationsAdapter.items = items
    }

    constructor(cont: Context, items: Array<LocationElement>) : super() {
        LocationsAdapter.items = items
        context = cont
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Inflate the layout, initialize the View Holder
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.location_list_item,
            parent, false
        )
        val holder: ViewHolder = ViewHolder(v)
        context = parent.context
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleLocation.text = items[position].titleLocation
        holder.longitude.text = items[position].longitude
        holder.latitude.text = items[position].latitude
        holder.numOfReviews.text = items[position].numOfReviews
        holder.numOfPhotos.text = items[position].numOfPhotos
        holder.itemView.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, LocationShowActivity::class.java)
            intent.putExtra("position", position)
            context.startActivity(intent)
        })
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

    companion object {
        lateinit var items: Array<LocationElement>
    }
}