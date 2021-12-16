package com.example.omy

import android.app.Application
import com.example.omy.data.OMYDatabase

class MainRepository (application: Application){
    val databaseObj: OMYDatabase? = OMYDatabase.getDatabase(application)

}