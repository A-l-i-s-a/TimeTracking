package com.example.timetracking.ui.listTask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timetracking.R
import com.example.timetracking.database.Task
import com.example.timetracking.database.TaskDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import timber.log.Timber

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
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ListTaskViewModel::class.java)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.tasks.observe(viewLifecycleOwner, Observer {
            recyclerView.adapter = TaskAdapter(it)
        })

        val floatingActionButton =
            view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        floatingActionButton.setOnClickListener(View.OnClickListener {
            this.findNavController().navigate(
                R.id.action_listTaskFragment_to_addTaskFragment
            )
        })
        return view
    }
}
