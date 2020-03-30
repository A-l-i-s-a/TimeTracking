package com.example.timetracking.ui.addTask

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.example.timetracking.R

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
        viewModel = ViewModelProviders.of(this).get(AddTaskViewModel::class.java)

        val button = view.findViewById<Button>(R.id.buttonCreateTask)

        button.setOnClickListener {
            viewModel
        }

        return view
    }
}
