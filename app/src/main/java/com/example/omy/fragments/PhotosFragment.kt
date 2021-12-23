package com.example.omy.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.omy.R
import com.example.omy.data.Image
import com.example.omy.data.LocationWithImages
import com.example.omy.locations.LocationsViewModel
import com.example.omy.photos.*
import com.example.omy.trips.TripsViewModel

import java.util.ArrayList

class PhotosFragment : Fragment() {
    private lateinit var searchView: SearchView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var recyclerEmpty: TextView
    private lateinit var mAdapter: Adapter<RecyclerView.ViewHolder>
    private var photosDataset: List<Image> = ArrayList<Image>()
    private var locationsWithPhotosDataset: List<LocationWithImages> = ArrayList<LocationWithImages>()
    private var tripsDataset: List<LocationWithImages> = ArrayList<LocationWithImages>()
    private var photosExtraInfoDataset: List<String> = ArrayList<String>()

    private var photosViewModel: PhotosViewModel? = null
    private var locationsViewModel: LocationsViewModel? = null
    private var tripsViewModel: TripsViewModel? = null

    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val pos = result.data?.getIntExtra("position", -1)!!
                val id = result.data?.getIntExtra("id", -1)!!
                val del_flag = result.data?.getIntExtra("deletion_flag", -1)!!
                if (pos != -1 && id != -1) {
                    if (result.resultCode == Activity.RESULT_OK) {
                        when(del_flag){
                            -1, 0 -> mAdapter.notifyDataSetChanged()
                            else -> mAdapter.notifyItemRemoved(pos)
                        }
                    }
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationsViewModel = ViewModelProvider(this)[LocationsViewModel::class.java]
        tripsViewModel = ViewModelProvider(this)[TripsViewModel::class.java]

        searchView = view.findViewById(R.id.photos_search_bar)
        recyclerEmpty = view.findViewById(R.id.no_photos)
        this.photosViewModel = ViewModelProvider(this)[PhotosViewModel::class.java]
        mAdapter = PhotosAdapter(photosDataset) as Adapter<RecyclerView.ViewHolder>
        initData()
        //initSearchData()
        mRecyclerView = view.findViewById(R.id.photos_list)
        val numberOfColumns = 4
        mRecyclerView.layoutManager = GridLayoutManager(requireContext(), numberOfColumns)

        mRecyclerView.adapter = mAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                query!!.trim()
                var filteredPhotos = photosDataset as ArrayList<Image>
                filteredPhotos.filter {
                    val allData = StringBuilder()
                        .append(it.id).append(it.imageDate)
                        .append(it.imageTitle).append(it.imageUri)
                        .toString().trim()
                    allData.replace(" ", "")
                        .contains(query!!, ignoreCase = true) }

                mAdapter = PhotosAdapter(filteredPhotos) as RecyclerView.Adapter<RecyclerView.ViewHolder>
                mRecyclerView.adapter = mAdapter
                return true
            }
        })
    }

    /**
     * Initialize the photos data from database
     *
     * @return List of photos data
     */
    private fun initData() {
        this.photosViewModel!!.getPhotosToDisplay()?.observe(viewLifecycleOwner, {newValue ->
            photosDataset = newValue
            mAdapter.notifyDataSetChanged()
            mAdapter = PhotosAdapter(newValue) as RecyclerView.Adapter<RecyclerView.ViewHolder>
            if (newValue.isEmpty()) recyclerEmpty.visibility = View.VISIBLE
            else recyclerEmpty.visibility = View.GONE

            //val idx = photosDataset.get(0).id
            //val elems = PhotosAdapter.items
        })
    }

    // Ahh not that far from finishing
    /*private fun initSearchData() {
        this.locationsViewModel!!.getLocationPhotosToDisplay()!!.observe(viewLifecycleOwner, { newValueLocations ->
            locationsWithPhotosDataset = newValueLocations

            this.tripsViewModel!!.getTripsToDisplay()!!.observe(viewLifecycleOwner, { newValueTrip ->
                //trips = newValue

                for (photo in photosDataset) {
                    val photoId = photo.id
                    for (imagePhoto in newValueLocations) {
                        imagePhoto.imageIdList.
                    }
                }
            })
        })
    }*/
}