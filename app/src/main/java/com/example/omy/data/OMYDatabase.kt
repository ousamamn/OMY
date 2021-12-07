package com.example.omy.data
import android.content.Context
import androidx.room.*
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Trip::class,Location::class,ImageLocation::class,Review::class,Image::class], version =1, exportSchema = false)
abstract class OMYDatabase: RoomDatabase()  {
    abstract fun TripDao(): TripDao
    abstract fun ImageLocationDao(): ImageLocationDao
    abstract fun LocationDao(): LocationDao
    abstract fun ReviewDao(): ReviewDao

    companion object{
        @Volatile
        private var INSTANCE: OMYDatabase? = null
        fun getDatabase(context: Context): OMYDatabase{
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OMYDatabase::class.java,
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