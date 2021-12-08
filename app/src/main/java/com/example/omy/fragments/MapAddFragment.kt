package com.example.omy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.omy.R


class MapAddFragment : Fragment() {
    private var displayMessage: String? = ""
    private lateinit var displayTitle: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_map_add, container, false)
        displayMessage = arguments?.getString("message")

        displayTitle = view.findViewById(com.example.omy.R.id.display_title)

        displayTitle.text = displayMessage

        return view
    }

}