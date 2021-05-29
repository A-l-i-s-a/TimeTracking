package com.example.timetracking.ui.chart

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
class ChartViewModel @ViewModelInject constructor(
    private val dataSource: TaskRepository
) : ViewModel() {
    private val _todo: MutableLiveData<DataState<List<Task>>> = MutableLiveData()

    val todo: LiveData<DataState<List<Task>>>
        get() = _todo

    private val _done: MutableLiveData<DataState<List<Task>>> = MutableLiveData()

    val done: LiveData<DataState<List<Task>>>
        get() = _done

    private val _byDate: MutableLiveData<DataState<List<Task>>> = MutableLiveData()

    val byDate: LiveData<DataState<List<Task>>>
        get() = _byDate


    fun loadData() {
        viewModelScope.launch {
            dataSource.getDoneTasks()
                .onEach { dataState ->
                    _done.value = dataState
                }
                .launchIn(viewModelScope)
            dataSource.getTodoTasks()
                .onEach { dataState ->
                    _todo.value = dataState
                }
                .launchIn(viewModelScope)
            dataSource.getTasksByDate(ListTasksStateEvent.GetTaskByDateEvents.date)
                .onEach { dataState ->
                    _byDate.value = dataState
                }
                .launchIn(viewModelScope)
        }
    }
}

sealed class ListTasksStateEvent {
    object GetTodoTasksEvents : ListTasksStateEvent()
    object GetDoneTasksEvents : ListTasksStateEvent()
    object GetTaskByDateEvents : ListTasksStateEvent() {
        var date = Date().time
    }

    object Synchronization : ListTasksStateEvent()
    object None : ListTasksStateEvent()
}
