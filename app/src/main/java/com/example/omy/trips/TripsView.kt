package com.example.omy.trips

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.omy.R
import com.example.omy.databinding.TripsActivityBinding

class TripsView: AppCompatActivity() {
    private var tripsViewModel: TripsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = TripsActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)

        this.tripsViewModel = ViewModelProvider(this)[TripsViewModel::class.java]
        /*this.tripsViewModel!!.getTripsToDisplay()!!.observe(this,
            {
                newValue ->
                val tv: TextView = findViewById(R.id.textView)
                if(newValue == null) tv.text = "click button"
                else tv.text = newValue.number.toString()
            }
        )*/
    }
}
