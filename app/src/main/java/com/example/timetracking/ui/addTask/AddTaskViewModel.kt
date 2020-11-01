package com.example.timetracking.ui.addTask

import android.app.Application
import android.app.NotificationManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import com.example.timetracking.database.Task
import com.example.timetracking.database.TaskDatabaseDao
import com.example.timetracking.util.sendNotification
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

    fun addTask(task: Task) {
        uiScope.launch {
            insert(task)
        }
        val notificationManager = ContextCompat.getSystemService(
            getApplication(),
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendNotification("Add task", getApplication(), task.id)
    }

}
