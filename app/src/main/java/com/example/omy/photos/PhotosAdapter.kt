package com.example.omy.photos

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.omy.R
import com.example.omy.data.Image
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/*
* PhotosAdapter.kt
* Mneimneh, Sekulski, Ooi 2021
* COM31007
*/
class PhotosAdapter : RecyclerView.Adapter<PhotosAdapter.ViewHolder> {
    private lateinit var context: Context

    constructor(items: List<Image>): super() {
        PhotosAdapter.items = items as MutableList<Image>
    }
    constructor() {
        PhotosAdapter.items = ArrayList<Image>()
    }
    constructor(cont: Context, items: List<Image>) : super() {
        PhotosAdapter.items = items as MutableList<Image>
        context = cont
    }

    /**
     * Function to update the list of photos
     *
     * @param parent A ViewGroup for photos
     * @param viewType Adapter's position
     * @return the list of photos
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Inflate the layout, initialize the View Holder
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.photo_list_item, parent, false)
        val holder: ViewHolder = ViewHolder(v)
        context = parent.context
        return holder
    }

    /**
     * Sets up the view holder for a Photo
     *
     * @param holder Photo's ViewHolder
     * @param position Photo's position/id
     * @return void
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Use the provided View Holder on the onCreateViewHolder method to populate the
        // current row on the RecyclerView
        if (items[position].thumbnail == null) {
            items[position].let {
                scope.launch {
                    val bitmap = decodeSampledBitmapFromResource(it.imageUri, 300, 300)
                    bitmap.let {
                        items[position].thumbnail = it
                        holder.imageView.setImageBitmap(items[position].thumbnail)
                    }
                }
            }
        }
        holder.imageView.setOnClickListener {
            val intent = Intent(context, PhotoShowActivity::class.java)
            intent.putExtra("position", position)
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

    class ViewHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById<View>(R.id.photo_item) as ImageView

    }

    companion object {
        lateinit var items: MutableList<Image>
        private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
        var locationWithPhotos: MutableList<Pair<Int,Pair<String?,String?>>> = ArrayList()

        suspend fun decodeSampledBitmapFromResource(filePath: String, reqWidth: Int, reqHeight: Int): Bitmap {
            // First decode with inJustDecodeBounds=true to check dimensions
            val options = BitmapFactory.Options()

            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(filePath, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false
            return BitmapFactory.decodeFile(filePath, options);
        }

        /**
         * Calculate the width and height of photo
         *
         * @param options BitmapFactory.Options
         * @param reqHeight Photo's height
         * @param reqWidth Photo's width
         * @return the size of the image
         */
        private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
            // Raw height and width of image
            val height = options.outHeight;
            val width = options.outWidth
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {
                val halfHeight = (height / 2)
                val halfWidth = (width / 2)

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2;
                }
            }
            return inSampleSize
        }
    }
}