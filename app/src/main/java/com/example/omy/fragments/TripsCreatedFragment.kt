package com.example.omy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.omy.Communicator
import com.google.android.material.floatingactionbutton.FloatingActionButton


class TripsCreatedFragment : Fragment() {

    private var displayMessage: String? = ""
    private lateinit var displayTitle: TextView
    private lateinit var addButton: FloatingActionButton
    private lateinit var communicator: Communicator


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(com.example.omy.R.layout.trips_created_fragment, container, false)

        displayMessage = arguments?.getString("message")

        displayTitle = view.findViewById(com.example.omy.R.id.display_title)

        displayTitle.text = displayMessage

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        communicator = activity as Communicator

        addButton = view.findViewById(com.example.omy.R.id.add_picture)
        addButton.setOnClickListener() {
            communicator.passLatitudeLongitude(displayTitle.text.toString())
        }
    }
}