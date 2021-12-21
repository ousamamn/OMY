package com.example.omy.reviews

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.omy.R
import com.example.omy.data.Review

class LocationReviewsAdapter: RecyclerView.Adapter<LocationReviewsAdapter.ViewHolder> {
    private lateinit var context: Context
    private var rating: Int = 0

    constructor(reviews: List<Review>): super() {
        LocationReviewsAdapter.reviews = reviews as MutableList<Review?>
    }
    constructor() {
        reviews = ArrayList()
    }
    constructor(cont: Context, reviews: List<Review>) : super() {
        LocationReviewsAdapter.reviews = reviews as MutableList<Review?>
        context = cont
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Inflate the layout, initialize the View Holder
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.location_review_list_item,
            parent, false)
        val holder = ViewHolder(v)
        context = parent.context
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = reviews[position]?.reviewTitle
        holder.description.text = reviews[position]?.reviewDescription
        holder.rating.text = rating.toString()
        holder.itemView.setOnClickListener {
            val intent = Intent(context, LocationReviewsActivity::class.java)
            intent.putExtra("position", position)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById<View>(R.id.review_title) as TextView
        var description: TextView = itemView.findViewById<View>(R.id.review_description) as TextView
        var rating: TextView = itemView.findViewById<View>(R.id.review_rating) as TextView
    }

    companion object {
        lateinit var reviews: MutableList<Review?>
    }
}