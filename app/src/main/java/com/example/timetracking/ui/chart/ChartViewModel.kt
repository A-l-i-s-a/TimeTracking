package com.example.timetracking.ui.chart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.timetracking.database.Task
import com.example.timetracking.database.TaskDatabaseDao
import kotlinx.coroutines.*

class ChartViewModel(
    private val dataSource: TaskDatabaseDao,
    application: Application
) : AndroidViewModel(application) {
    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val tasks = dataSource.getAllTasks()

    fun findTodoTasks(): List<Task> = runBlocking {
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

    fun findDoneTasks(): List<Task> = runBlocking {
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

    fun getDoneTasksOnEachDay(): Map<String, Float> {
        val map = mapOf<String, Float>()
        val findDoneTasks = findDoneTasks()
        return map
    }

    fun getDoneTaskByDay(day: String) {

    }
}