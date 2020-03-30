package com.example.timetracking.ui.listTask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.timetracking.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListTaskFragment : Fragment() {

    companion object {
        fun newInstance() = ListTaskFragment()
    }

    private lateinit var viewModel: ListTaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.list_task_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(ListTaskViewModel::class.java)
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
