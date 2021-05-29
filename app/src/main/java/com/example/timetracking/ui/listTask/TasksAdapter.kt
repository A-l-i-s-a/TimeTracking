package com.example.timetracking.ui.listTask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.timetracking.R
import com.example.timetracking.database.Task
import com.example.timetracking.util.formatTime


class TasksAdapter(
    private var list: List<Task>,
    private val listener: Listener
) :
    RecyclerView.Adapter<TasksAdapter.TasksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.task_item, parent, false)
        return TasksViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class TasksViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val beginning = view.findViewById<TextView>(R.id.textViewTimeBeginning)
        private val end = view.findViewById<TextView>(R.id.textViewTimeEnd)
        private val title = view.findViewById<TextView>(R.id.textViewTitle)
        private val description = view.findViewById<TextView>(R.id.textViewDescription)
        private val isNeedSynchronization = view.findViewById<ImageView>(R.id.isNeedSynchronizationImageView)

        fun bind(task: Task) {
            beginning.text = task.timeBeginning?.let { formatTime(it) }
            end.text = task.timeEnd?.let { formatTime(it) }
            title.text = task.headline
            description.text = task.description
            isNeedSynchronization.visibility = if (task.isNeedSynchronization) View.VISIBLE else View.GONE
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onItemClick(list[adapterPosition])
                }
            }
        }
    }

    interface Listener {
        fun onItemClick(task: Task)
    }
}