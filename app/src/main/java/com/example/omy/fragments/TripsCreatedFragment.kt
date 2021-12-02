package com.example.omy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.omy.R

class TripsCreatedFragment : Fragment() {

    private var displayMessage: String? = ""
    private lateinit var displayTitle: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.trips_created_fragment, container, false)

        displayMessage = arguments?.getString("message")

        displayTitle = view.findViewById(R.id.display_title)

        displayTitle.text = displayMessage

        return view
    }
}