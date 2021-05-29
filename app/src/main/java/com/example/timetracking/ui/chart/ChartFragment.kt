package com.example.timetracking.ui.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.example.timetracking.R
import com.example.timetracking.database.Task
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate.PASTEL_COLORS
import com.example.timetracking.util.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.add_task_fragment.*
import kotlinx.android.synthetic.main.fragment_chart.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class ChartFragment : Fragment() {

    private val viewModel: ChartViewModel by viewModels()
    private var map: HashMap<String, List<Task>> = hashMapOf()
    val todo_key = "todo"
    val done_key = "done"
    val by_date_key = "byDate"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chart, container, false)
        subscribeObservers(viewModel.todo, todo_key)
        subscribeObservers(viewModel.done, done_key)
        subscribeObservers(viewModel.byDate, by_date_key)
        viewModel.loadData()
        return view
    }

    fun setBarEntry(): List<BarEntry> {
        return  listOf<BarEntry>()
    }

    private fun subscribeObservers(tasks: LiveData<DataState<List<Task>>>, key: String) {
        tasks.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is DataState.Success<List<Task>> -> {
                    displayProgressBar(false)
                    map[key] = it.data
                    if (map.size == 2) {
                        val todoList = map[todo_key]
                        val doneList = map[done_key]
                        val byDateList = map[by_date_key]

                        val countTodoTasks = (todoList!!.size).toFloat()
                        val countDoneTasks = (doneList!!.size).toFloat()
                        val pieEntryTodo = listOf<PieEntry>(
                            PieEntry(countTodoTasks, "TO DO"),
                            PieEntry(countDoneTasks, "DONE")
                        )
                        val pieDataSetTodo = PieDataSet(pieEntryTodo, "")
                        pieDataSetTodo.colors = PASTEL_COLORS.asList()
                        val pieData = PieData(pieDataSetTodo)
                        pieChart.data = pieData
                        pieChart.invalidate()

                        val barEntryDone =
                            listOf<BarEntry>(BarEntry(0F, (doneList.size).toFloat()))

                        val barDataSet = BarDataSet(barEntryDone, "Done")
                        val barData = BarData(barDataSet)

                        barChartDone.data = barData
                        barChartDone.invalidate()

                        val barEntryTodo =
                            listOf<BarEntry>(BarEntry(1F, (todoList.size).toFloat()))
                        val barDataSetTodo = BarDataSet(barEntryTodo, "Todo")
                        val barDataTodo = BarData(barDataSetTodo)

                        barChartTodo.data = barDataTodo
                        barChartTodo.invalidate()
                    }
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
        progressBar2.visibility = if (isDisplay) View.VISIBLE else View.GONE
    }

}
