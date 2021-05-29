package com.example.timetracking.ui.listTask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timetracking.R
import com.example.timetracking.database.Task
import com.example.timetracking.util.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.list_task_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class ListTaskFragment : Fragment() {

    companion object {
        fun newInstance() = ListTaskFragment()
    }

    private val viewModel: ListTaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_task_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribeObservers()
        recyclerView.layoutManager = LinearLayoutManager(context)
        val listTasksStateEvent = ListTasksStateEvent.GetTaskByDateEvents
        viewModel.setStateEvent(listTasksStateEvent)

//        var selectCalendar: Calendar = Calendar.getInstance()
//        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
//            selectCalendar = GregorianCalendar(year, month, dayOfMonth)
//            listTasksStateEvent.date = selectCalendar.timeInMillis
//            viewModel.setStateEvent(listTasksStateEvent)
//        }
        chartButton.setOnClickListener {
            findNavController().navigate(R.id.action_listTaskFragment_to_chartFragment)
        }

        uploadImageView.setOnClickListener {
            viewModel.setStateEvent(ListTasksStateEvent.Synchronization)
        }

        chipAll.setOnClickListener {
            viewModel.setStateEvent(ListTasksStateEvent.GetAllTasksEvents)
        }
        chipToday.setOnClickListener {
            viewModel.setStateEvent(listTasksStateEvent)
        }
        chipDone.setOnClickListener {
            viewModel.setStateEvent(ListTasksStateEvent.GetDoneTasksEvents)
        }
        chipTodo.setOnClickListener {
            viewModel.setStateEvent(ListTasksStateEvent.GetTodoTasksEvents)
        }
//        var date = OffsetDateTime.now()
//        chipByDate.setOnClickListener {
//            val now = LocalDate.now()
//            val mDay = now.dayOfMonth
//            val mMonth = now.month.ordinal
//            val mYear = now.year
//
//            // Launch Date Picker Dialog
//            context?.let {
//                DatePickerDialog(
//                    it,
//                    DatePickerDialog.OnDateSetListener { _, year, month, day ->
//                        date = date.withYear(year)
//                        date = date.withMonth(month + 1)
//                        date = date.withDayOfMonth(day)
//                        viewModel.getTaskByDate(date).observe(viewLifecycleOwner, Observer { list ->
//                            recyclerView.adapter = TaskAdapter(list, taskListener)
//                        })
//                    },
//                    mYear,
//                    mMonth,
//                    mDay
//                )
//            }?.show()
//        }

        floatingActionButton.setOnClickListener {
            this.findNavController().navigate(
                R.id.action_listTaskFragment_to_addTaskFragment
            )
        }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is DataState.Success<List<Task>> -> {
                    displayProgressBar(false)
                    displayTasks(it.data)
                }
                is DataState.Error -> {
                    displayProgressBar(false)
                    displayError(it.exception.message)
                }
                is DataState.Loading -> {
                    displayProgressBar(true)
                }
            }
        })
    }

    private fun displayError(message: String?) {
        if (message != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Unknown error", Toast.LENGTH_LONG).show()
        }
    }

    private fun displayProgressBar(isDisplay: Boolean) {
        progressBar.visibility = if (isDisplay) View.VISIBLE else View.GONE
    }

    private fun displayTasks(tasks: List<Task>) {
        val adapter = TasksAdapter(tasks, object : TasksAdapter.Listener {
            override fun onItemClick(task: Task) {
                findNavController().navigate(
                    R.id.action_listTaskFragment_to_taskFragment, bundleOf("task" to task)
                )
            }
        })
        recyclerView.adapter = adapter
    }
}
