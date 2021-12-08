package com.example.omy

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.example.omy.databinding.MainActivityBinding
import com.example.omy.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), Communicator {

    private lateinit var mMap: GoogleMap
    private lateinit var ntButton: Button
    private lateinit var goButton: Button
    private lateinit var cancelButton: Button
    private lateinit var tnEditText: EditText
    private lateinit var weatherTemperatureText: TextView
    private lateinit var weatherIconView: ImageView

    private val simpleFragment = TripsCreatedFragment()
    private val homeFragment = HomeFragment()
    private val locationsFragment = LocationsFragment()
    private val tripsFragment = TripsFragment()
    private val picturesFragment = PicturesFragment()

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(simpleFragment)
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> replaceFragment(homeFragment)
                R.id.navigation_locations -> replaceFragment(locationsFragment)
                R.id.navigation_trips -> replaceFragment(tripsFragment)
                R.id.navigation_pictures -> replaceFragment(picturesFragment)
            }
            true
        }
    }

    override fun passDataCom(editTextInput: String) {
        val bundle = Bundle()
        bundle.putString("message", editTextInput)

        val transaction = this.supportFragmentManager.beginTransaction()
        val tripsCreatedFragment = TripsCreatedFragment()
        tripsCreatedFragment.arguments = bundle

        transaction.replace(R.id.fragment_container, tripsCreatedFragment)
        transaction.commit()
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment !== null) {
            val transaction =  supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }

    override fun passLatitudeLongitude(textView: String) {
        val bundle = Bundle()
        bundle.putString("message", textView.toString())

        val transaction = this.supportFragmentManager.beginTransaction()
        val mapAddFragment = MapAddFragment()
        mapAddFragment.arguments = bundle

        transaction.replace(R.id.fragment_container, mapAddFragment)
        transaction.commit()
    }

}