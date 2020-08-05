package com.example.timetracking.database

import androidx.lifecycle.LiveData
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
    fun insert(task: Task)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param task new value to write
     */
    @Update
    fun update(task: Task)

    /**
     * Selects and returns the row that matches the supplied id, which is our key.
     *
     * @param key id to match
     */
    @Query("SELECT * from task_table WHERE id = :key")
    fun get(key: Long): Task?

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
    fun getAllTasks(): LiveData<List<Task>>

    /**
     * Selects and returns rows of done tasks in the table.
     */
    @Query("SELECT * FROM task_table WHERE todo_is_done = 1 ORDER BY id DESC")
    fun getDoneTasks(): List<Task>

    /**
     * Selects and returns rows of not done tasks in the table.
     */
    @Query("SELECT * FROM task_table WHERE todo_is_done = 0 ORDER BY id DESC")
    fun getTodoTasks(): List<Task>
}