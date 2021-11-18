package com.example.omy

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter : RecyclerView.Adapter<MyAdapter.ViewHolder> {
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
            R.layout.list_item_image,
            parent, false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Use the provided View Holder on the onCreateViewHolder method to populate the
        // current row on the RecyclerView
        if (items[position] != null) {
            holder.title.text = items[position].title
            holder.preview.text = items[position].preview
            holder.imageView.setImageResource(items[position].image)
        }
        //animate(holder);
    }

    override fun getItemCount(): Int {
        return items.size
    }

    public class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var image: Image
        var title: TextView = itemView.findViewById<View>(R.id.title) as TextView
        var preview: TextView = itemView.findViewById<View>(R.id.preview) as TextView
        var imageView: ImageView = itemView.findViewById<View>(R.id.image_item) as ImageView

    }
}