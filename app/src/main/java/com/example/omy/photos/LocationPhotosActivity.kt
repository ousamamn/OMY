package com.example.omy.photos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.omy.R
import com.example.omy.data.Image
import com.example.omy.data.Review
import com.example.omy.locations.LocationShowActivity
import com.example.omy.locations.LocationsViewModel
import java.util.ArrayList

class LocationPhotosActivity : AppCompatActivity() {
    private lateinit var mRecyclerViewPhotos: RecyclerView
    private lateinit var mPhotosAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    private lateinit var mPhotosLayoutManager: RecyclerView.LayoutManager
    private var photosDataset: List<Image?> = ArrayList<Image?>()
    private var photosViewModel: PhotosViewModel? = null
    private var locationsViewModel: LocationsViewModel? = null
    private lateinit var photosRecyclerEmpty: TextView
    private lateinit var displayTitle: TextView
    private lateinit var backToPrevious: ImageView
    var locationId: Int? = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_photos_activity)

        // Display the photo data
        photosViewModel = ViewModelProvider(this)[PhotosViewModel::class.java]
        locationsViewModel = ViewModelProvider(this)[LocationsViewModel::class.java]

        photosRecyclerEmpty = findViewById(R.id.no_photos)
        mRecyclerViewPhotos = findViewById(R.id.photos_item)
        mPhotosLayoutManager = LinearLayoutManager(this)
        mRecyclerViewPhotos.layoutManager = mPhotosLayoutManager
        mPhotosAdapter = PhotosAdapter(photosDataset as List<Image>) as RecyclerView.Adapter<RecyclerView.ViewHolder>
        initDataPhotos()
        mPhotosAdapter.notifyDataSetChanged()
        if (photosDataset.isNotEmpty()) {
            PhotosAdapter(photosDataset as List<Image>)
        }
        mRecyclerViewPhotos.adapter = mPhotosAdapter

        // Get the data from the database and pass it into the activity
        val b: Bundle? = intent.extras
        var locationTitle: String? = "Title"
        if (b != null) {
            locationTitle = b.getString("locationTitle")
            locationId = b.getInt("locationPosition") +1
            displayTitle = findViewById(R.id.title_name)
            displayTitle.text = locationTitle
        }

        // Back to previous page
        backToPrevious = findViewById(R.id.back_to_previous)
        backToPrevious.setOnClickListener {
            onBackPressed()
            finish()
        }
    }

    /**
     * Initialize the photos data from database
     *
     * @return List of photos data
     */
    private fun initDataPhotos() {
        this.locationsViewModel!!.getLocationPhotosToDisplay()!!.observe(this, { newValue ->
            for (unit in newValue){
                if (unit.location.id == locationId){
                    Log.i("TAGELLOO",locationId.toString())
                    photosDataset = unit.imageIdList as MutableList<Image?>
                }
            }
            mPhotosAdapter  = PhotosAdapter(photosDataset as List<Image>) as RecyclerView.Adapter<RecyclerView.ViewHolder>
            mPhotosAdapter.notifyDataSetChanged()
            if (newValue.isEmpty()) photosRecyclerEmpty.visibility = View.VISIBLE
            else photosRecyclerEmpty.visibility = View.GONE
            mRecyclerViewPhotos.adapter = mPhotosAdapter
        })
    }
}