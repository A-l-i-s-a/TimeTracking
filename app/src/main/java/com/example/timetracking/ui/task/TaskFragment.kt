package com.example.timetracking.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.timetracking.R
import com.example.timetracking.database.Task
import com.example.timetracking.database.TaskDatabase

class TaskFragment : Fragment() {

    private lateinit var viewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.task_fragment, container, false)

        val headline = view.findViewById<TextView>(R.id.headlineTask)
        val todoIsDone = view.findViewById<TextView>(R.id.todoIsDoneTask)
        val timeBeginning = view.findViewById<TextView>(R.id.timeBeginningTask)
        val timeEnd = view.findViewById<TextView>(R.id.timeEndTask)
        val description = view.findViewById<TextView>(R.id.descriptionTask)
        val place = view.findViewById<TextView>(R.id.placeTask)

        val application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        val viewModelFactory = TaskViewModelFactory(dataSource, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(TaskViewModel::class.java)
        val bundle: Bundle? = arguments
        val task: Task
        if (bundle != null) {
            task = bundle.get("task") as Task
            println("task = $task")
            headline.text = task.headline
            description.text = task.description
            todoIsDone.text = if (task.todoIsDone) "DONE" else "TO DO"
            timeBeginning.text = task.timeBeginning
            timeEnd.text = task.timeEnd
            place.text = task.place
        }
        return view
    }
}
