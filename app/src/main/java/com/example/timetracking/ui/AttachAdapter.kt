package com.example.timetracking.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.timetracking.R

import com.squareup.picasso.Picasso

class AttachAdapter(private var list: List<Uri>, private val listener: Listener) :
    RecyclerView.Adapter<AttachAdapter.TasksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TasksViewHolder(inflater.inflate(R.layout.attach_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class TasksViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val attachNameTextView = view.findViewById<TextView>(R.id.attachNameTextView)
        private val attachImageView = view.findViewById<ImageView>(R.id.attachImageView)

        fun bind(uri: Uri) {

            attachNameTextView.text = uri.lastPathSegment?.split("/")!!.last()
            if (!uri.scheme.equals("content")) {
                Picasso.get().load(uri).fit().into(attachImageView)
            }
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) listener.onItemClick(list[adapterPosition])
            }
        }
    }

    interface Listener {
        fun onItemClick(uri: Uri)
    }
}