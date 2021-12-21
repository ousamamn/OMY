package com.example.omy.locations

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.omy.R
import com.example.omy.data.Location

class LocationsAdapter : RecyclerView.Adapter<LocationsAdapter.ViewHolder> {
    private lateinit var context: Context
    private var numOfReviews: Int = 0
    private var numOfPhotos: Int = 0


    constructor(items: List<Location?>): super() {
        LocationsAdapter.items = items as MutableList<Location?>
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Inflate the layout, initialize the View Holder
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.location_list_item,
            parent, false)
        val holder = ViewHolder(v)
        context = parent.context
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleLocation.text = items[position]?.locationTitle
        holder.longitude.text = "%.2f".format(items[position]?.locationLongitude)
        holder.latitude.text = "%.2f".format(items[position]?.locationLatitude)
        holder.numOfReviews.text = numOfReviews.toString()
        holder.numOfPhotos.text = numOfPhotos.toString()
        holder.itemView.setOnClickListener {
            val intent = Intent(context, LocationShowActivity::class.java)
            intent.putExtra("position", position)
            context.startActivity(intent)
        }
    }

    fun updateChildTables(numOfReviews : Int,numOfPhotos : Int) {
        this.numOfPhotos = numOfPhotos
        this.numOfReviews = numOfReviews
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleLocation: TextView = itemView.findViewById<View>(R.id.location_title) as TextView
        var longitude: TextView = itemView.findViewById<View>(R.id.location_longitude) as TextView
        var latitude: TextView = itemView.findViewById<View>(R.id.location_latitude) as TextView
        var numOfReviews: TextView = itemView.findViewById<View>(R.id.location_num_of_reviews) as TextView
        var numOfPhotos: TextView = itemView.findViewById<View>(R.id.location_num_of_photos) as TextView

    }

    companion object {
        lateinit var items: MutableList<Location?>
    }
}