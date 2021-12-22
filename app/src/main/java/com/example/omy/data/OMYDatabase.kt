package com.example.omy.data
import android.content.Context
import android.telecom.Call
import androidx.room.*
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

@Database(entities = [Trip::class,Location::class,ImageLocation::class,Review::class,Image::class], version =5, exportSchema = false)
abstract class OMYDatabase: RoomDatabase() {
    abstract fun TripDao(): TripDao
    abstract fun ImageLocationDao(): ImageLocationDao
    abstract fun LocationDao(): LocationDao
    abstract fun ReviewDao(): ReviewDao
    abstract fun ImageDao(): ImageDao

    companion object {
        private var INSTANCE: OMYDatabase? = null
        private val mutex = Mutex()
        fun getDatabase(context: Context): OMYDatabase? {
            if (INSTANCE == null) {
                runBlocking {
                    withContext(Dispatchers.Default) {
                        mutex.withLock(OMYDatabase::class) {
                            INSTANCE = databaseBuilder(
                                context.applicationContext,
                                OMYDatabase::class.java, "main_database"
                            )
                                .fallbackToDestructiveMigration()
                                //.addCallback(sRoomDatabaseCallback)
                                .build()
                        }
                    }
                }
            }
            return INSTANCE
        }
        /*private val sRoomDatabaseCallback: RoomDatabase.Callback = object : Callback(){
            override fun onOpen(db: SupportSQLiteDatabase){
                super.onOpen(db)
            }
        }*/
    }
}