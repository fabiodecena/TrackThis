package com.example.trackthis.data.database.tracked_topic

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * [HistoryDatabase] is the Room database class for the application.
 * It manages the storage and retrieval of [TrackedTopic] entities.
 *
 * The database uses a [DailyTimeSpentConverter] to handle the conversion
 * of complex data types.
 *
 * The database is a singleton, ensuring only one instance is created
 * across the application's lifecycle.
 */
@Database(entities = [TrackedTopic::class], version = 1, exportSchema = false)
@TypeConverters(DailyTimeSpentConverter::class)
abstract class HistoryDatabase : RoomDatabase() {
    /**
     * Provides access to the [TrackedTopicDao], which handles database
     * operations for [TrackedTopic] entities.
     */
    abstract fun trackedTopicDao(): TrackedTopicDao

    companion object {
        @Volatile
        private var Instance: HistoryDatabase? = null
        /**
         * Returns the singleton instance of the [HistoryDatabase].
         *
         * If an instance does not exist, it creates a new one using
         * Room's database builder.
         *
         * @param context The application context.
         * @return The singleton instance of [HistoryDatabase].
         */
        fun getDatabase(context: Context): HistoryDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, HistoryDatabase::class.java, "history_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}