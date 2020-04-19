package com.example.timetracking.ui.addTask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.timetracking.R
import com.example.timetracking.database.Task
import com.example.timetracking.database.TaskDatabase
import timber.log.Timber

class AddTaskFragment : Fragment() {

    companion object {
        fun newInstance() = AddTaskFragment()
    }

    private lateinit var viewModel: AddTaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_task_fragment, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        val viewModelFactory = AddTaskViewModelFactory(dataSource, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddTaskViewModel::class.java)

        val button = view.findViewById<Button>(R.id.buttonCreateTask)

        val headline = view.findViewById<TextView>(R.id.editTextHeadline)

        val description = view.findViewById<TextView>(R.id.editTextDescription)

        val place = view.findViewById<TextView>(R.id.editTextPlace)

        val timeBeginning = view.findViewById<TextView>(R.id.inputTimeBeginning)

        val timeEnd = view.findViewById<TextView>(R.id.inputTimeEnd)

        button.setOnClickListener {
            viewModel.addTask(
                Task(
                    headline = headline.text.toString(),
                    place = place.text.toString(),
                    description = description.text.toString(),
                    timeBeginning = timeBeginning.text.toString(),
                    timeEnd = timeEnd.text.toString()
                )
            )
            Timber.i("click on button 'Create'")
            this.findNavController().popBackStack()
        }

        return view
    }
}
