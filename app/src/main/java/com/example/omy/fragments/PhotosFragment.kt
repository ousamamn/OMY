package com.example.omy.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.omy.R
import com.example.omy.data.Image
import com.example.omy.photos.*
import com.example.omy.trips.TripsViewModel

import java.util.ArrayList

class PhotosFragment : Fragment() {
    lateinit var searchView: SearchView
    lateinit var mRecyclerView: RecyclerView
    private  var mAdapter: PhotosAdapter = PhotosAdapter()
    private val photoDataset: MutableList<Image> = ArrayList<Image>()
    private var photosViewModel: PhotosViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.photosViewModel = ViewModelProvider(this)[PhotosViewModel::class.java]

        initData()
        mRecyclerView = view.findViewById(R.id.photos_list)
        val numberOfColumns = 4
        mRecyclerView.layoutManager = GridLayoutManager(requireContext(), numberOfColumns)

        //mAdapter = PhotosAdapter(photoDataset) as Adapter<RecyclerView.ViewHolder>
        mRecyclerView.adapter = mAdapter
    }

    private fun initData() {
        this.photosViewModel!!.getPhotosToDisplay()?.observe(viewLifecycleOwner, {newValue ->
            mAdapter.updatePhotoList(newValue)
        })
    }
}