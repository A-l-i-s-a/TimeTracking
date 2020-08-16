package com.example.timetracking.ui.listTask

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.timetracking.database.Task
import com.example.timetracking.database.TaskDatabaseDao
import kotlinx.coroutines.*
import java.time.OffsetDateTime

class ListTaskViewModel(
    private val dataSource: TaskDatabaseDao,
    application: Application
) : AndroidViewModel(application) {
    /** Coroutine setup variables */

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = Job()

    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val tasks = dataSource.getAllTasks()

    val todoTasks = runBlocking {
        findTodo()
    }

    private suspend fun findTodo(): List<Task> {
        val async = uiScope.async(Dispatchers.IO) {
            val list = async {
                dataSource.getTodoTasks()
            }
            list.await()
        }
        return async.await()
    }

    val doneTasks = runBlocking {
        findDone()
    }

    private suspend fun findDone(): List<Task> {
        val async = uiScope.async(Dispatchers.IO) {
            val list = async {
                dataSource.getDoneTasks()
            }
            list.await()
        }
        return async.await()
    }

    fun getTaskByDate(date: OffsetDateTime): LiveData<List<Task>> = dataSource.getTaskByDate(date)

    private suspend fun update(task: Task) {
        withContext(Dispatchers.IO) {
            dataSource.update(task)
        }
    }

    fun updateTask(task: Task) {
        uiScope.launch {
            update(task)
        }
    }

}
