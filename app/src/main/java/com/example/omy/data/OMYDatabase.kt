package com.example.omy.data
import android.content.Context
import androidx.room.*
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Trip::class], version = 1, exportSchema = false)
abstract class OMY_Database: RoomDatabase()  {
    abstract fun TripDao(): TripDao

    companion object{
        @Volatile
        private var INSTANCE: OMY_Database? = null
        fun getDatabase(context: Context): OMY_Database{
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OMY_Database::class.java,
                    "lab5_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object specified.
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                return instance
            }
        }
    }
}