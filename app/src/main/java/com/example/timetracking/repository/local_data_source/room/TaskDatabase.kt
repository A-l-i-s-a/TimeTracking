package com.example.timetracking.repository.local_data_source.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * A database that stores Task information.
 * And a global method to get access to the database.
 */
@Database(entities = [TaskCacheEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateConverters::class)
abstract class TaskDatabase : RoomDatabase() {

    /**
     * Connects the database to the DAO.
     */
    abstract val taskDatabaseDao: TaskDatabaseDao
}