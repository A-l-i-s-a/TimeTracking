package com.example.timetracking.ui.addTask

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.timetracking.database.Task
import com.example.timetracking.database.TaskDatabaseDao
import kotlinx.coroutines.*
import timber.log.Timber

class AddTaskViewModel(
    private val database: TaskDatabaseDao,
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

    private suspend fun insert(task: Task) {
        withContext(Dispatchers.IO) {
            database.insert(task)
            Timber.i("Insert task")
        }
    }

    fun addTask(task: Task){
        uiScope.launch {
            insert(task)
        }
    }

}
