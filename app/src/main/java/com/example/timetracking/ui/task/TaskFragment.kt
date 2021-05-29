package com.example.timetracking.ui.task

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timetracking.R
import com.example.timetracking.database.Task
import com.example.timetracking.ui.AttachAdapter
import com.example.timetracking.util.formatTime
import com.example.timetracking.util.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.task_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.File

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class TaskFragment : Fragment() {

    companion object {
        fun newInstance() = TaskFragment()
    }

    private val viewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.task_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribeObservers()
        attachTaskRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val bundle: Bundle? = arguments
        if (bundle != null) {
            viewModel.getTask(bundle.getLong("id"))
        }
    }

    private fun setFieldTask(task: Task) {
        headlineTask.text = task.headline
        descriptionTask.text = task.description
        todoIsDoneTask.text = if (task.todoIsDone) "DONE" else "TO DO"
        timeBeginningTask.text = task.timeBeginning?.let { formatTime(it) }
        timeEndTask.text = task.timeEnd?.let { formatTime(it) }
        placeTask.text = task.place
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is DataState.Success<Task?> -> {
                    displayProgressBar(false)
                    it.data?.let { it1 -> displayTask(it1) }
                }
                is DataState.Error -> {
                    displayProgressBar(false)
                    displayError(it.exception.message)
                }
                is DataState.Loading -> {
                    displayProgressBar(true)
                }
            }
        })
    }

    private fun displayError(message: String?) {
        if (message != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Unknown error", Toast.LENGTH_LONG).show()
        }
    }

    private fun displayProgressBar(isDisplay: Boolean) {
        progressBarForItemTask.visibility = if (isDisplay) View.VISIBLE else View.GONE
    }

    private fun displayTask(task: Task) {
        setFieldTask(task)
        setAdapter(task)
    }

    private fun setAdapter(task: Task) {
        attachTaskRecyclerView.adapter =
            AttachAdapter(task.attachments, object : AttachAdapter.Listener {
                override fun onItemClick(uri: Uri) {
                    try {
                        val newURI = FileProvider.getUriForFile(
                            context!!,
                            context!!.applicationContext.packageName.toString() + ".provider",
                            File(uri.path)
                        )
                        val intent = Intent()
                        intent.action = Intent.ACTION_VIEW
                        intent.setDataAndType(newURI, "*/*")
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        startActivity(intent)
                    } catch (e: IllegalArgumentException) {
                        displayError(e.localizedMessage)
                    }
                }
            })
    }
}
