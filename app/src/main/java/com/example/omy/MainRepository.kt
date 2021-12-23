package com.example.omy

import android.app.Application
import com.example.omy.data.OMYDatabase

/*
* MainRepository.kt
* Mneimneh, Sekulski, Ooi 2021
* COM31007
*/
class MainRepository (application: Application){
    val databaseObj: OMYDatabase? = OMYDatabase.getDatabase(application)
}