package com.example.timetracking.ui.listTask

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timetracking.R
import com.example.timetracking.database.Task
import com.example.timetracking.database.TaskDatabase
import com.example.timetracking.ui.listTask.TaskAdapter.TaskViewHolder.Listener
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.time.OffsetDateTime

class ListTaskFragment : Fragment() {

    private lateinit var viewModel: ListTaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.list_task_fragment, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        val viewModelFactory = ListTaskViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ListTaskViewModel::class.java)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(context)

        val taskListener = object : Listener {
            override fun onItemClick(task: Task) {
                findNavController().navigate(
                    R.id.action_listTaskFragment_to_taskFragment, bundleOf("task" to task)
                )
            }

            override fun onCheckBoxClickListener(task: Task) {
                viewModel.updateTask(task)
            }
        }

        viewModel.tasks.observe(viewLifecycleOwner, Observer {
            recyclerView.adapter = TaskAdapter(it, taskListener)
        })

        val chartButton = view.findViewById<Button>(R.id.chartButton)
        chartButton.setOnClickListener {
            findNavController().navigate(R.id.action_listTaskFragment_to_chartFragment)
        }

        view.findViewById<Chip>(R.id.chipAll).setOnClickListener {
            viewModel.tasks.observe(viewLifecycleOwner, Observer {
                recyclerView.adapter = TaskAdapter(it, taskListener)
            })
        }
        view.findViewById<Chip>(R.id.chipToday).setOnClickListener {
            viewModel.getTaskByDate(OffsetDateTime.now()).observe(viewLifecycleOwner, Observer {
                recyclerView.adapter = TaskAdapter(it, taskListener)
            })
        }
        view.findViewById<Chip>(R.id.chipDone).setOnClickListener {
            recyclerView.adapter = TaskAdapter(viewModel.doneTasks, taskListener)
        }
        view.findViewById<Chip>(R.id.chipTodo).setOnClickListener {

            recyclerView.adapter = TaskAdapter(viewModel.todoTasks, taskListener)
        }
        var date = OffsetDateTime.now()
        view.findViewById<Chip>(R.id.chipByDate).setOnClickListener {
            val now = LocalDate.now()
            val mDay = now.dayOfMonth
            val mMonth = now.month.ordinal
            val mYear = now.year

            // Launch Date Picker Dialog
            context?.let {
                DatePickerDialog(
                    it,
                    DatePickerDialog.OnDateSetListener { _, year, month, day ->
                        date = date.withYear(year)
                        date = date.withMonth(month + 1)
                        date = date.withDayOfMonth(day)
                        viewModel.getTaskByDate(date).observe(viewLifecycleOwner, Observer { list ->
                            recyclerView.adapter = TaskAdapter(list, taskListener)
                        })
                    },
                    mYear,
                    mMonth,
                    mDay
                )
            }?.show()
        }


        val floatingActionButton =
            view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        floatingActionButton.setOnClickListener {
            this.findNavController().navigate(
                R.id.action_listTaskFragment_to_addTaskFragment
            )
        }
        return view
    }
}
