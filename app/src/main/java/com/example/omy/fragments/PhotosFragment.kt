package com.example.omy.fragments

import android.annotation.SuppressLint
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
import com.example.omy.photos.*
import kotlinx.coroutines.runBlocking
import pl.aprilapps.easyphotopicker.ChooserType
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile

import java.util.ArrayList

class PhotosFragment : Fragment() {
    private lateinit var searchView: SearchView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var recyclerEmpty: TextView
    private lateinit var mAdapter: Adapter<RecyclerView.ViewHolder>
    private val photoDataset: MutableList<Image> = ArrayList<Image>()
    private lateinit var easyImage: EasyImage
    private var photosViewModel: PhotosViewModel? = null

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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerEmpty = view.findViewById(R.id.no_photos)
        this.photosViewModel = ViewModelProvider(this)[PhotosViewModel::class.java]
        mAdapter = PhotosAdapter(photoDataset) as Adapter<RecyclerView.ViewHolder>
        initData()
        mRecyclerView = view.findViewById(R.id.photos_list)
        val numberOfColumns = 4
        mRecyclerView.layoutManager = GridLayoutManager(requireContext(), numberOfColumns)

        mRecyclerView.adapter = mAdapter
    }

    private fun initData() {
        this.photosViewModel!!.getPhotosToDisplay()?.observe(viewLifecycleOwner, {newValue ->
            mAdapter.notifyDataSetChanged()
            mAdapter = PhotosAdapter(newValue) as RecyclerView.Adapter<RecyclerView.ViewHolder>
            if (newValue.isEmpty()) recyclerEmpty.visibility = View.VISIBLE
            else recyclerEmpty.visibility = View.GONE
        })
    }

}