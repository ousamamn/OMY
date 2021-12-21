package com.example.omy.reviews

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.omy.R
import com.example.omy.data.Review

class ReviewsAdapter: RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    private lateinit var context: Context

    constructor(items: List<Review>): super() {
        ReviewsAdapter.items = items as MutableList<Review?>
    }
    constructor() {
        ReviewsAdapter.items = ArrayList<Review?>()
    }
    constructor(cont: Context, items: List<Review>) : super() {
        ReviewsAdapter.items = items as MutableList<Review?>
        context = cont
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsAdapter.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ReviewsAdapter.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById<View>(R.id.review_title) as TextView
        var description: TextView = itemView.findViewById<View>(R.id.review_description) as TextView
        var rating: TextView = itemView.findViewById<View>(R.id.review_rating) as TextView
    }

    companion object {
        lateinit var items: MutableList<Review?>
    }
}