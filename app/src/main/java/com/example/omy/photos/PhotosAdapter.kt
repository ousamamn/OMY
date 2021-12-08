package com.example.omy.photos

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.omy.R

class PhotosAdapter : RecyclerView.Adapter<PhotosAdapter.ViewHolder> {
    private lateinit var context: Context

    constructor(items: List<PhotoElement>): super() {
        PhotosAdapter.items = items
    }

    constructor(cont: Context, items: List<PhotoElement>) : super() {
        PhotosAdapter.items = items
        context = cont
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Inflate the layout, initialize the View Holder
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.photo_list_item,
            parent, false
        )
        val holder: ViewHolder = ViewHolder(v)
        context = parent.context
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Use the provided View Holder on the onCreateViewHolder method to populate the
        // current row on the RecyclerView
        if (items[position].image != -1) {
            holder.imageView.setImageResource(items[position].image)
        }
        holder.imageView.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, PhotoShowActivity::class.java)
            intent.putExtra("position", position)
            context.startActivity(intent)
        })
    }



    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById<View>(R.id.photo_item) as ImageView
    }

    companion object {
        lateinit var items: List<PhotoElement>
    }
}