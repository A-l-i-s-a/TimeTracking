package com.example.timetracking.ui.task

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.timetracking.database.Task
import com.example.timetracking.database.TaskDatabaseDao
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class TaskViewModel(
    private val dataSource: TaskDatabaseDao,
    application: Application
) : AndroidViewModel(application) {
    /** Coroutine setup variables */

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = Job()
    private var _task: Task? = Task()

    val task: Task?
        get() = _task


    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private suspend fun getTask(id: Long): Task? {
        return withContext(IO) {
            dataSource.get(id)
        }
    }

    fun findTaskById(id: Long) {
        uiScope.launch {
            _task = getTask(id)
            println("_task = $_task")
        }
    }

    private suspend fun update(task: Task) {
        withContext(IO) {
            dataSource.update(task)
        }
    }

    fun updateTask(task: Task) {
        uiScope.launch {
            update(task)
        }
    }
}
