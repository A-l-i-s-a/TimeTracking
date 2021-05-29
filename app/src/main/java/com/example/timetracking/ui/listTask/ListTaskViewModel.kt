package com.example.timetracking.ui.listTask

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timetracking.database.Task
import com.example.timetracking.repository.TaskRepository
import com.example.timetracking.util.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalCoroutinesApi
class ListTaskViewModel @ViewModelInject constructor(
    private val dataSource: TaskRepository
) : ViewModel() {
    private val _dataState: MutableLiveData<DataState<List<Task>>> = MutableLiveData()

    val dataState: LiveData<DataState<List<Task>>>
        get() = _dataState

    fun setStateEvent(listTasksStateEvent: ListTasksStateEvent) {
        viewModelScope.launch {
            when (listTasksStateEvent) {
                is ListTasksStateEvent.GetAllTasksEvents -> {
                    dataSource.getTasks()
                        .onEach { dataState ->
                            _dataState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }
                is ListTasksStateEvent.GetDoneTasksEvents -> {
                    dataSource.getDoneTasks()
                        .onEach { dataState ->
                            _dataState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }
                is ListTasksStateEvent.GetTodoTasksEvents -> {
                    dataSource.getTodoTasks()
                        .onEach { dataState ->
                            _dataState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }
                is ListTasksStateEvent.GetTaskByDateEvents -> {
                    dataSource.getTasksByDate(ListTasksStateEvent.GetTaskByDateEvents.date)
                        .onEach { dataState ->
                            _dataState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }
                is ListTasksStateEvent.Synchronization -> {
                    dataSource.synchronization().launchIn(viewModelScope)
                }
                is ListTasksStateEvent.None -> {
                    //
                }

            }
        }
    }
}

sealed class ListTasksStateEvent {
    object GetAllTasksEvents : ListTasksStateEvent()
    object GetTodoTasksEvents : ListTasksStateEvent()
    object GetDoneTasksEvents : ListTasksStateEvent()
    object GetTaskByDateEvents : ListTasksStateEvent() {
        var date = Date().time
    }

    object Synchronization : ListTasksStateEvent()
    object None : ListTasksStateEvent()
}
