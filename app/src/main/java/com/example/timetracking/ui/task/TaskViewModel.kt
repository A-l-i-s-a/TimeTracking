package com.example.timetracking.ui.task

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.timetracking.database.Task
import com.example.timetracking.repository.TaskRepository
import com.example.timetracking.util.DataState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
class TaskViewModel @ViewModelInject constructor(
    private val dataSource: TaskRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _dataState: MutableLiveData<DataState<Task?>> = MutableLiveData()

    val dataState: LiveData<DataState<Task?>>
        get() = _dataState

    fun getTask(id: Long){
        viewModelScope.launch {
            dataSource.getTaskById(id)
                .onEach {
                    _dataState.value = it
                }
                .launchIn(viewModelScope)
        }
    }
}
