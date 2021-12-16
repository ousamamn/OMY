package com.example.omy.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.runBlocking
import pl.aprilapps.easyphotopicker.ChooserType
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile

import java.util.ArrayList

class PhotosFragment : Fragment() {
    lateinit var searchView: SearchView
    lateinit var mRecyclerView: RecyclerView
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

        this.photosViewModel = ViewModelProvider(this)[PhotosViewModel::class.java]

        initData()
        mRecyclerView = view.findViewById(R.id.photos_list)
        val numberOfColumns = 4
        mRecyclerView.layoutManager = GridLayoutManager(requireContext(), numberOfColumns)
        mAdapter = PhotosAdapter(photoDataset) as Adapter<RecyclerView.ViewHolder>

        //mAdapter = PhotosAdapter(photoDataset) as Adapter<RecyclerView.ViewHolder>
        mRecyclerView.adapter = mAdapter

        initEasyImage()
       // val fabGallery: FloatingActionButton = getView().findViewById(pl.aprilapps.easyphotopicker.R.id.fab_gallery)
        //fabGallery.setOnClickListener(View.OnClickListener {
        //    easyImage.openChooser(this@PhotosFragment)
        //})
    }

    private fun initEasyImage() {
        easyImage = EasyImage.Builder(requireContext())
//        .setChooserTitle("Pick media")
//        .setFolderName(GALLERY_DIR)
            .setChooserType(ChooserType.CAMERA_AND_GALLERY)
            .allowMultiple(true)
//        .setCopyImagesToPublicGalleryFolder(true)
            .build()
    }

    private fun initData() {
        this.photosViewModel!!.getPhotosToDisplay()?.observe(viewLifecycleOwner, {newValue ->
            photoDataset.addAll(newValue)
        })
    }

    private fun insertData(imageData: Image): Int = runBlocking {
        var insertJob = photosViewModel!!.createNewPhoto(imageData)
        insertJob.toString().toInt()
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun onPhotosReturned(returnedPhotos: Array<MediaFile>) {
        photoDataset.addAll(getImageData(returnedPhotos))

        // we tell the adapter that the data is changed and hence the grid needs
        mAdapter.notifyDataSetChanged()
        mRecyclerView.scrollToPosition(returnedPhotos.size - 1)
    }

    /**
     * given a list of photos, it creates a list of ImageData objects
     * we do not know how many elements we will have
     * @param returnedPhotos
     * @return
     */
    private fun getImageData(returnedPhotos: Array<MediaFile>): List<Image> {
        val imageDataList: MutableList<Image> = ArrayList<Image>()
        for (mediaFile in returnedPhotos) {
            val fileNameAsTempTitle = mediaFile.file.name
            var imageData = Image(
                imageTitle = fileNameAsTempTitle,
                imageUri = mediaFile.file.absolutePath
            )
            // Update the database with the newly created object
//            var id = insertData(imageData)
            var id = insertData(imageData)
            imageData.id = id.toString().toInt()
            imageDataList.add(imageData)
        }
        return imageDataList
    }

}