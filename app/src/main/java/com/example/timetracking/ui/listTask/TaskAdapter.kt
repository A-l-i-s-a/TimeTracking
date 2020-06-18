package com.example.timetracking.ui.listTask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.timetracking.R
import com.example.timetracking.database.Task
import timber.log.Timber

class TaskAdapter(private val list: List<Task>, private val listener: TaskViewHolder.Listener) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_task_item, parent, false)
        view.setOnClickListener { v ->
            listener.onItemClick(v.tag as Task)
        }
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task: Task = list[position]
        Timber.i("%s = %s", position, task.timeBeginning)
        holder.bind(task, listener)
        holder.itemView.tag = task
    }

    override fun getItemCount(): Int = list.size


    class TaskViewHolder(v: View) :
        RecyclerView.ViewHolder(v){

        private val beginning = v.findViewById<TextView>(R.id.textViewTimeBeginning)
        private val end = v.findViewById<TextView>(R.id.textViewTimeEnd)
        private val headline = v.findViewById<TextView>(R.id.textViewHeadline)
        private val description = v.findViewById<TextView>(R.id.textViewDescription)
        private var todoIsDone = v.findViewById<CheckBox>(R.id.checkBoxTask)

        fun bind(task: Task, listener: Listener) {
            beginning.text = task.timeBeginning
            end.text = task.timeEnd
            headline.text = task.headline
            description.text = task.description
            todoIsDone.isChecked = task.todoIsDone
            todoIsDone.setOnCheckedChangeListener{ buttonView, isChecked ->
                listener.onCheckedChangeListener(task, isChecked)
            }
            Timber.d("element is create: %s", task)
        }

        interface Listener {
            fun onItemClick(task: Task)
            fun onCheckedChangeListener(task: Task, isChecked: Boolean)
        }


    }

}