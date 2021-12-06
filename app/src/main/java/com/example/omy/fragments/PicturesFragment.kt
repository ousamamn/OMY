package com.example.omy.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.omy.R
import com.example.omy.photos.*

import java.util.ArrayList

class PicturesFragment : Fragment() {
    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: Adapter<RecyclerView.ViewHolder>
    private val photoDataset: MutableList<PhotoElement> = ArrayList<PhotoElement>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pictures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        /*  Get list of trips */
        mRecyclerView = view.findViewById(R.id.photos_list)
        val numberOfColumns = 4
        mRecyclerView.layoutManager = GridLayoutManager(requireContext(), numberOfColumns)

        mAdapter = PhotosAdapter(photoDataset) as Adapter<RecyclerView.ViewHolder>
        mRecyclerView.adapter = mAdapter
    }

    private fun initData() {
        repeat(5){
            photoDataset.add(PhotoElement(R.drawable.joe1))
            photoDataset.add(PhotoElement(R.drawable.joe2))
            photoDataset.add(PhotoElement(R.drawable.joe3))
        }
    }
}