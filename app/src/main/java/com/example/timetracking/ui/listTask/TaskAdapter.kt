package com.example.timetracking.ui.listTask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
        Timber.i("%s = %s", position, task.timeBeginning)
        print("" + position + " = " + task.timeBeginning)
        holder.bind(task)
    }

    override fun getItemCount(): Int = list.size


    class TaskViewHolder(v: View) :
        RecyclerView.ViewHolder(v), View.OnClickListener {
        private val beginning = v.findViewById<TextView>(R.id.textViewTimeBeginning)
        private val end = v.findViewById<TextView>(R.id.textViewTimeEnd)
        private val headline = v.findViewById<TextView>(R.id.textViewHeadline)
        private val description = v.findViewById<TextView>(R.id.textViewDescription)

        fun bind(task: Task) {
            beginning.text = task.timeBeginning
            end.text = task.timeEnd
            headline.text = task.headline
            description.text = task.description
            Timber.d("'element is create: %s", task)

        }

        override fun onClick(v: View?) {
            Timber.d("CLICK!")
        }
    }

}