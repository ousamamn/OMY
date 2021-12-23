package com.example.omy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.omy.databinding.MainActivityBinding
import com.example.omy.fragments.HomeFragment
import com.example.omy.fragments.LocationsFragment
import com.example.omy.fragments.PhotosFragment
import com.example.omy.fragments.TripsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

/*
* MainActivity.kt
* This file provides a bottom navigation
  bar and set the Home Fragment
  as the primary page.
* Mneimneh, Sekulski, Ooi 2021
* COM31007
*/
class MainActivity : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val locationsFragment = LocationsFragment()
    private val tripsFragment = TripsFragment()
    private val photosFragment = PhotosFragment()

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bottom navigation bar with home fragment as the initial page
        replaceFragment(homeFragment)
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> replaceFragment(homeFragment)
                R.id.navigation_locations -> replaceFragment(locationsFragment)
                R.id.navigation_trips -> replaceFragment(tripsFragment)
                R.id.navigation_photos -> replaceFragment(photosFragment)
            }
            true
        }
    }

    /**
     * Switches between the fragments in place
     *
     * @return void
     */
    private fun replaceFragment(fragment: Fragment) {
        if (fragment !== null) {
            val transaction =  supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }
}