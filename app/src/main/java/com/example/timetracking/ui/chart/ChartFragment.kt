package com.example.timetracking.ui.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.timetracking.R
import com.example.timetracking.database.TaskDatabase
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate.PASTEL_COLORS

class ChartFragment : Fragment() {

    private lateinit var viewModel: ChartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chart, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        val viewModelFactory = ChartViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ChartViewModel::class.java)

        val pieChart = view.findViewById<PieChart>(R.id.pieChart)
        val countTodoTasks = (viewModel.findTodoTasks().size).toFloat()
        val countDoneTasks = (viewModel.findDoneTasks().size).toFloat()
        val pieEntryTodo = listOf<PieEntry>(
            PieEntry(countTodoTasks, "TO DO"),
            PieEntry(countDoneTasks, "DONE")
        )
        val pieDataSetTodo = PieDataSet(pieEntryTodo, "")
        pieDataSetTodo.colors = PASTEL_COLORS.asList()
        val pieData = PieData(pieDataSetTodo)
        pieChart.data = pieData
        pieChart.invalidate()

        val barChartDone = view.findViewById<BarChart>(R.id.barChartDone)
        val barEntryDone =
            listOf<BarEntry>(BarEntry(0F, (viewModel.findDoneTasks().size).toFloat()))

        val barDataSet = BarDataSet(barEntryDone, "Done")
        val barData = BarData(barDataSet)

        barChartDone.data = barData
        barChartDone.invalidate()

        val barChartTodo = view.findViewById<BarChart>(R.id.barChartTodo)
        val barEntryTodo =
            listOf<BarEntry>(BarEntry(1F, (viewModel.findTodoTasks().size).toFloat()))
        val barDataSetTodo = BarDataSet(barEntryTodo, "Todo")
        val barDataTodo = BarData(barDataSetTodo)

        barChartTodo.data = barDataTodo
        barChartTodo.invalidate()

        return view
    }

    fun setBarEntry(): List<BarEntry> {
        return  listOf<BarEntry>()
    }

}
