package com.example.omy.maps

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.example.omy.R
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.view.ViewGroup

import android.view.View

import android.view.View.OnTouchListener

import android.widget.EditText





class MapAddActivity : AppCompatActivity() {
    private lateinit var displayTitle: TextView
    private lateinit var cancelButton: Button
    private lateinit var saveButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_add_activity)
        val b: Bundle? = intent.extras

        var msg: String? = "Title"
        if (b != null) {
            msg = b.getString("msg")
            displayTitle = findViewById(R.id.display_title_map_add)
            displayTitle.text = msg
        }
        val titleNameEditText = findViewById<EditText>(R.id.title_name_edit_text)
        val descriptionEditText = findViewById<EditText>(R.id.description_name_edit_text)
        cancelButton = findViewById(R.id.cancel_button)
        saveButton = findViewById(R.id.save_button)
        cancelButton.setOnClickListener {
            onBackPressed()
            finish()
        }
        saveButton.setOnClickListener {
            if (TextUtils.isEmpty(titleNameEditText.text.toString())) {
                Toast.makeText(
                    applicationContext,
                    "Please Insert The Title",
                    Toast.LENGTH_SHORT
                )
                    .show();
            } else if (TextUtils.isEmpty(descriptionEditText.text.toString())) {
                Toast.makeText(
                    applicationContext,
                    "Please Insert The Description",
                    Toast.LENGTH_SHORT
                )
                    .show();
            } else {
                onBackPressed()
                finish()
                // the onBackPressed need to be change to be able to save the reviews
                //val intent = Intent(context, MapsCreatedActivity::class.java)
                //val msg = tnEditText.text.toString()
                //intent.putExtra("msg", msg)
                //context?.startActivity(intent)
            }
        }
        setupUI(findViewById(R.id.on_touch))
    }
    private fun hideSoftKeyboard(mapAddActivity: MapAddActivity) {
        val inputMethodManager: InputMethodManager = getSystemService(
            INPUT_METHOD_SERVICE
        ) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(
                currentFocus!!.windowToken,
                0
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupUI(view: View) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener(OnTouchListener { v, event ->
                hideSoftKeyboard(this)
                false
            })
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until (view as ViewGroup).childCount) {
                val innerView: View = (view as ViewGroup).getChildAt(i)
                setupUI(innerView)
            }
        }
    }
}