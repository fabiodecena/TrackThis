package com.example.trackthis.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//Database class with a singleton Instance object.
@Database(entities = [TrackedTopic::class], version = 2, exportSchema = false)
abstract class HistoryDatabase : RoomDatabase() {

    abstract fun trackedTopicDao(): TrackedTopicDao

    companion object {
        @Volatile
        private var Instance: HistoryDatabase? = null

        fun getDatabase(context: Context): HistoryDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, HistoryDatabase::class.java, "history_database")
//                    .createFromAsset("database/history.db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}