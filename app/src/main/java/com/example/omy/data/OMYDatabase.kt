package com.example.omy.data
import android.content.Context
import androidx.room.*
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Trip::class,Location::class,ImageLocation::class,Review::class,Image::class], version =1, exportSchema = false)
abstract class OMYDatabase: RoomDatabase(), TripDao {
    abstract fun TripDao(): TripDao
    abstract fun ImageLocationDao(): ImageLocationDao
    abstract fun LocationDao(): LocationDao
    abstract fun ReviewDao(): ReviewDao

    companion object{
        @Volatile
        private var INSTANCE: OMYDatabase? = null
        fun getDatabase(context: Context): OMYDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OMYDatabase::class.java,
                    "omy_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                return instance
            }
        }
    }
}