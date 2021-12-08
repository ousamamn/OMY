package com.example.omy.trips

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class TripsView: AppCompatActivity() {
    private var tripsViewModel: TripsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        this.tripsViewModel = ViewModelProvider(this)[TripsViewModel::class.java]
        this.tripsViewModel!!.getTripsToDisplay()!!.ob
    }
}
