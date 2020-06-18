package com.example.timetracking.ui.listTask

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.timetracking.database.Task
import com.example.timetracking.database.TaskDatabaseDao
import kotlinx.coroutines.*

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
