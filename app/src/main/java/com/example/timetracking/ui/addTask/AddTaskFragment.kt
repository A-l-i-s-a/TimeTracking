package com.example.timetracking.ui.addTask

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.timetracking.R
import com.example.timetracking.database.Task
import com.example.timetracking.database.TaskDatabase
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime


class AddTaskFragment : Fragment() {

    companion object {
        fun newInstance() = AddTaskFragment()
    }

    private lateinit var viewModel: AddTaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_task_fragment, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).taskDatabaseDao
        val viewModelFactory = AddTaskViewModelFactory(dataSource, application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddTaskViewModel::class.java)

        val button = view.findViewById<Button>(R.id.buttonCreateTask)
        val headline = view.findViewById<TextView>(R.id.editTextHeadline)
        val description = view.findViewById<TextView>(R.id.editTextDescription)
        val place = view.findViewById<TextView>(R.id.editTextPlace)
        val timeBeginning = view.findViewById<TextView>(R.id.inputTimeBeginning)
        val timeEnd = view.findViewById<TextView>(R.id.inputTimeEnd)
        val dateBeginning = view.findViewById<TextView>(R.id.inputDateBeginning)
        val dateEnd = view.findViewById<TextView>(R.id.inputDateEnd)

        var beginningDate: LocalDate = LocalDate.now()
        var endDate: LocalDate = LocalDate.now()
        var beginningTime: LocalTime = LocalTime.now()
        var endTime: LocalTime = LocalTime.now()

        timeBeginning.text = beginningTime.toString()
        timeEnd.text = endTime.toString()

        dateBeginning.text = beginningDate.toString()
        dateEnd.text = endDate.toString()

        dateBeginning.setOnClickListener {
            onClickDate { date ->
                run {
                    beginningDate = date
                    dateBeginning.text = formatDate(beginningDate)
                }
            }
        }

        dateEnd.setOnClickListener {
            onClickDate { date ->
                run {
                    endDate = date
                    dateEnd.text = formatDate(endDate)
                }
            }
        }

        timeBeginning.setOnClickListener {
            onClickTime { time ->
                run {
                    beginningTime = time
                    timeBeginning.text = formatTime(beginningTime)
                }
            }
        }

        timeEnd.setOnClickListener {
            onClickTime { time ->
                run {
                    endTime = time
                    timeEnd.text = formatTime(endTime)
                }
            }
        }

        button.setOnClickListener {
            viewModel.addTask(
                Task(
                    headline = headline.text.toString(),
                    place = place.text.toString(),
                    description = description.text.toString(),
                    timeBeginning = OffsetDateTime.of(
                        beginningDate,
                        beginningTime,
                        OffsetDateTime.now().offset
                    ),
                    timeEnd = OffsetDateTime.of(
                        endDate,
                        endTime,
                        OffsetDateTime.now().offset
                    )
                )
            )
            Timber.i("click on button 'Create'")
            this.findNavController().popBackStack()
        }

        return view
    }

    private fun onClickTime(function: (LocalTime) -> Unit) {
        // Get Current Time
        val now = LocalTime.now()
        val mHour = now.hour
        val mMinute = now.minute

        // Launch Time Picker Dialog
        TimePickerDialog(
            context,
            OnTimeSetListener { _, hourOfDay, minute ->
                function(LocalTime.of(hourOfDay, minute))
            },
            mHour,
            mMinute,
            true
        ).show()
    }

    private fun onClickDate(function: (LocalDate) -> Unit) {
        // Get Current Date
        val now = LocalDate.now()
        val mDay = now.dayOfMonth
        val mMonth = now.month.value
        val mYear = now.year

        // Launch Date Picker Dialog
        context?.let {
            DatePickerDialog(
                it,
                OnDateSetListener { _, year, month, day ->
                    function(LocalDate.of(year, month, day))
                },
                mYear,
                mMonth,
                mDay
            )
        }?.show()
    }

    private fun formatDate(date: LocalDate): String {
        return String.format(
            "%s %s %s",
            date.dayOfMonth,
            date.month,
            date.year
        )
    }

    private fun formatTime(time: LocalTime): String {
        return String.format(
            "%s:%s",
            time.hour,
            time.minute
        )
    }
}
