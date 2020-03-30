package com.example.timetracking.ui.listTask

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.timetracking.R
import com.example.timetracking.database.Task
import timber.log.Timber

class TaskAdapter(private val list: List<Task>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TaskViewHolder(inflater.inflate(R.layout.list_task_item, parent, false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task: Task = list[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int = list.size


    class TaskViewHolder(v: View) :
        RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v

        init {
            view.setOnClickListener(this)
        }

        fun bind(task: Task) {

        }

        override fun onClick(v: View?) {
            Timber.d("CLICK!")
        }
    }

}