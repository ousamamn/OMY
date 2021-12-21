package com.example.omy.locations

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.omy.R
import com.example.omy.data.Image
import com.example.omy.data.Location
import com.example.omy.data.Review
import com.example.omy.data.Trip
import com.example.omy.photos.PhotosViewModel
import kotlinx.coroutines.runBlocking
import pl.aprilapps.easyphotopicker.*
import java.util.ArrayList
import com.example.omy.reviews.LocationAddReviewActivity
import com.example.omy.reviews.LocationReviewsActivity
import com.example.omy.reviews.LocationReviewsAdapter
import com.example.omy.reviews.ReviewsViewModel
import pl.aprilapps.easyphotopicker.ChooserType
import pl.aprilapps.easyphotopicker.EasyImage

class LocationShowActivity : AppCompatActivity() {
    private lateinit var mRecyclerViewReviews: RecyclerView
    private lateinit var mReviewsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    private lateinit var mReviewsLayoutManager: RecyclerView.LayoutManager
    private var reviewsDataset: List<Review?> = ArrayList<Review?>()
    private var reviewsViewModel: ReviewsViewModel? = null
    private lateinit var reviewsRecyclerEmpty: TextView

    private lateinit var mRecyclerViewPhotos: RecyclerView
    private lateinit var mPhotosAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    private lateinit var mPhotosLayoutManager: RecyclerView.LayoutManager
    //private var photosDataset: List<Photo?> = ArrayList<Review?>()
    private var locationsViewModel: LocationsViewModel? = null
     var element: Location? = null


    private lateinit var backButton: ImageView
    private lateinit var addReviewButton: TextView
    private lateinit var addPhotoButton: TextView
    private lateinit var seeAllReviewsButton: TextView
    private lateinit var seeAllPhotosButton: TextView
    private lateinit var easyImage: EasyImage
    private var imagesViewModel: PhotosViewModel? = null
    private var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_activity)
        reviewsViewModel = ViewModelProvider(this)[ReviewsViewModel::class.java]

        reviewsRecyclerEmpty = findViewById(R.id.no_reviews)
        mRecyclerViewReviews = findViewById(R.id.location_reviews)
        mReviewsLayoutManager = LinearLayoutManager(this)
        mRecyclerViewReviews.layoutManager = mReviewsLayoutManager
        mReviewsAdapter = LocationReviewsAdapter(reviewsDataset) as RecyclerView.Adapter<RecyclerView.ViewHolder>
        initData()

        mReviewsAdapter.notifyDataSetChanged()

        if (reviewsDataset.isNotEmpty()) {
            LocationReviewsAdapter(reviewsDataset)
        }


        //mRecyclerViewPhotos = findViewById(R.id.location_photos)
        //mPhotosLayoutManager = LinearLayoutManager(this)
        //mRecyclerViewPhotos.layoutManager = mPhotosLayoutManager
        //mPhotosAdapter = LocationReviewsAdapter()


        //mRecyclerViewPhotos.adapter = mPhotosAdapter

        imagesViewModel = ViewModelProvider(this)[PhotosViewModel::class.java]

        val b: Bundle? = intent.extras
        if (b != null) {
            position = b.getInt("position")
            if (position != -1) {
                Log.e("loc_position", position.toString())
                val textView = findViewById<TextView>(R.id.title_name)
                val textViewTitleInfo = findViewById<TextView>(R.id.location_title)
                val textViewLongitudeInfo = findViewById<TextView>(R.id.location_longitude)
                val textViewLatitudeInfo = findViewById<TextView>(R.id.location_latitude)
                val textViewDate = findViewById<TextView>(R.id.location_date)
                val textViewDescription = findViewById<TextView>(R.id.location_description)
                element = LocationsAdapter.items[position]
                if (element != null) {
                    textView.text = element!!.locationTitle
                    textViewTitleInfo.text = element!!.locationTitle
                    textViewLongitudeInfo.text = "%.2f".format(element!!.locationLongitude)
                    textViewLatitudeInfo.text = "%.2f".format(element!!.locationLatitude)
                    textViewDate.text = element!!.locationDate?.replace('-', ' ')
                    textViewDescription.text = element!!.locationDescription
                } else {
                    textView.text = "Error"
                    textViewTitleInfo.text = "Error"
                    textViewLongitudeInfo.text = "Error"
                    textViewLatitudeInfo.text = "Error"
                }
            }
        }

        backButton = findViewById(R.id.back_to_previous)
        addReviewButton = findViewById(R.id.location_review_add)
        seeAllReviewsButton = findViewById(R.id.location_reviews_see_all)
        seeAllPhotosButton = findViewById(R.id.location_photos_see_all)
        addPhotoButton = findViewById(R.id.location_photo_add)

        backButton.setOnClickListener {
            onBackPressed()
            finish()
        }
        addReviewButton.setOnClickListener {
            val intentNewReview = Intent(this, LocationAddReviewActivity::class.java)
            val textView = findViewById<TextView>(R.id.title_name)
            val reviewActivityTitle = textView.text.toString()
            intentNewReview.putExtra("locationTitle", reviewActivityTitle)
            intentNewReview.putExtra("locationPosition", position)
            startActivity(intentNewReview)
        }
        seeAllReviewsButton.setOnClickListener {
            val intentAllReviews = Intent(this, LocationReviewsActivity::class.java)
            val textView = findViewById<TextView>(R.id.title_name)
            val reviewActivityTitle = textView.text.toString()
            intentAllReviews.putExtra("locationTitle", reviewActivityTitle)
            intentAllReviews.putExtra("locationPosition", position)
            startActivity(intentAllReviews)
        }
        /*addPhotoButton.setOnClickListener {
            val easyImage: EasyImage = EasyImage.Builder(this)
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .setCopyImagesToPublicGalleryFolder(false)
                .setFolderName("EasyImage sample")
                //.allowMultiple(true)
                .build()
            easyImage.openGallery(this)
            //TODO("SAVE PICKED PHOTO")
        }
        seeAllPhotosButton.setOnClickListener {
            val intentForTitle = Intent(this, LocationReviewsActivity::class.java)
            val textView = findViewById<TextView>(R.id.title_name)
            val reviewActivityTitle = textView.text.toString()
            intentForTitle.putExtra("locationTitle", reviewActivityTitle)
            startActivity(intentForTitle)
        }*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        easyImage.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {
            override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                onPhotosReturned(imageFiles)
            }
            override fun onImagePickerError(error: Throwable, source: MediaSource) {
                super.onImagePickerError(error, source)
            }
            override fun onCanceled(source: MediaSource) {
                super.onCanceled(source)
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onPhotosReturned(returnedPhotos: Array<MediaFile>) {
       // myDataset.addAll(getImageData(returnedPhotos))

        // we tell the adapter that the data is changed and hence the grid needs
        Log.i("TAG",returnedPhotos[0].file.absolutePath)
    }

    private fun getImageData(returnedPhotos: Array<MediaFile>): List<Image> {
        val imageList: MutableList<Image> = ArrayList<Image>()
        for (mediaFile in returnedPhotos) {
            val fileNameAsTempTitle = mediaFile.file.name
            var image = Image(
                imageTitle = fileNameAsTempTitle,
                imageUri = mediaFile.file.absolutePath
            )
            // Update the database with the newly created object
            var id = insertData(image)
            image.id = id
            imageList.add(image)
        }
        return imageList
    }

    private fun insertData(image: Image): Int = runBlocking {
        val insertJob = imagesViewModel!!.createNewPhoto(image)
        insertJob
    }

    private fun initData() {
        this.reviewsViewModel!!.getReviewsToDisplay()!!.observe(this, { newValue ->
            reviewsDataset = getLocationReviews(element!!.id, newValue as MutableList<Review?>)
            val howManyDisplayed = 2
            if (reviewsDataset.size >= howManyDisplayed){
                reviewsDataset = reviewsDataset.takeLast(howManyDisplayed)
            }
            mReviewsAdapter = LocationReviewsAdapter(reviewsDataset) as RecyclerView.Adapter<RecyclerView.ViewHolder>
            mReviewsAdapter.notifyDataSetChanged()
            if (newValue.isEmpty()) reviewsRecyclerEmpty.visibility = View.VISIBLE
            else reviewsRecyclerEmpty.visibility = View.GONE
            mRecyclerViewReviews.adapter = mReviewsAdapter
        })

        //TODO("Not yet implemented  - PHOTOS")
    }

    companion object{
        fun getLocationReviews(locationID: Int, reviews:MutableList<Review?>):MutableList<Review?>{
            val result: MutableList<Review?> = ArrayList()
            for (review in reviews){
                if (review!!.reviewLocationId == locationID){
                    result.add(review)
                }
            }
            return result
        }
    }
}