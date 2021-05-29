package com.example.timetracking.repository.local_data_source.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Defines methods for using the Task class with Room.
 */
@Dao
interface TaskDatabaseDao {
    @Insert
    fun insert(task: TaskCacheEntity)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param task new value to write
     */
    @Update
    fun update(task: TaskCacheEntity)

    /**
     * Selects and returns the row that matches the supplied id, which is our key.
     *
     * @param key id to match
     */
    @Query("SELECT * from task_table WHERE id = :key")
    fun get(key: Long): TaskCacheEntity?

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM task_table")
    fun clear()

    /**
     * Selects and returns all rows in the table,
     * sorted by id in descending order.
     */
    @Query("SELECT * FROM task_table ORDER BY id DESC")
    fun getAllTasks(): List<TaskCacheEntity>

    /**
     * Selects and returns rows of done tasks in the table.
     */
    @Query("SELECT * FROM task_table WHERE todo_is_done = 1 ORDER BY id DESC")
    fun getDoneTasks(): List<TaskCacheEntity>

    /**
     * Selects and returns rows of not done tasks in the table.
     */
    @Query("SELECT * FROM task_table WHERE todo_is_done = 0 ORDER BY id DESC")
    fun getTodoTasks(): List<TaskCacheEntity>

    /**
     * Selects and returns rows by date tasks in the table.
     */
    @Query("SELECT * FROM task_table WHERE date(time_beginning) = date(:date) ORDER BY id DESC")
    fun getTaskByDate(date: Long): List<TaskCacheEntity>

    /**
     * Selects and returns rows by is need synchronization tasks in the table.
     */
    @Query("SELECT * FROM task_table WHERE is_need_synchronization = 1")
    fun getTasksByIsNeedSynchronization(): List<TaskCacheEntity>
}