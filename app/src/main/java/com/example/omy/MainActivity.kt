package com.example.omy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.omy.databinding.MainActivityBinding
import com.example.omy.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val locationsFragment = LocationsFragment()
    private val tripsFragment = TripsFragment()
    private val picturesFragment = PicturesFragment()

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(homeFragment)
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

//    override fun passDataCom(editTextInput: String) {
//        val bundle = Bundle()
//        bundle.putString("message", editTextInput)
//
//        val transaction = this.supportFragmentManager.beginTransaction()
//        val mapFragment = MapFragmentActivity()
//        mapFragment.arguments = bundle
//
//        transaction.replace(R.id.fragment_container, tripsCreatedFragment)
//        transaction.commit()
//    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment !== null) {
            val transaction =  supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }

}