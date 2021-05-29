package com.example.timetracking.ui.addTask

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timetracking.database.Task
import com.example.timetracking.repository.TaskRepository
import com.example.timetracking.util.DataState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
class AddTaskViewModel @ViewModelInject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _dataState: MutableLiveData<DataState<Task>> = MutableLiveData()

    val dataState: LiveData<DataState<Task>>
        get() = _dataState

    fun createTask(task: Task) {
        viewModelScope.launch {
            taskRepository.createTask(task)
                .onEach { dataState ->
                    _dataState.value = dataState
                }
                .launchIn(viewModelScope)
        }
    }

}
