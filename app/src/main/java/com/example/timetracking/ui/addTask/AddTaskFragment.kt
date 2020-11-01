package com.example.timetracking.ui.addTask

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.timetracking.R
import com.example.timetracking.database.Task
import com.example.timetracking.database.TaskDatabase
import com.example.timetracking.ui.dialog.CreateNotificationDialogFragment
import com.example.timetracking.util.formatDate
import com.example.timetracking.util.formatTime
import kotlinx.android.synthetic.main.add_task_fragment.view.*
import kotlinx.android.synthetic.main.create_notification.*
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime


class AddTaskFragment : Fragment(), CreateNotificationDialogFragment.CreateNotificationDialogListener {

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

        val button = view.buttonCreateTask
        val headline = view.editTextHeadline
        val description = view.editTextDescription
        val place = view.editTextPlace
        val timeBeginning = view.inputTimeBeginning
        val timeEnd = view.inputTimeEnd
        val dateBeginning = view.inputDateBeginning
        val dateEnd = view.inputDateEnd

        var beginningDate: LocalDate = LocalDate.now()
        var endDate: LocalDate = LocalDate.now()
        var beginningTime: LocalTime = LocalTime.now()
        var endTime: LocalTime = LocalTime.now()

        timeBeginning.text = formatTime(beginningTime)
        timeEnd.text = formatTime(endTime)

        dateBeginning.text = formatDate(beginningDate)
        dateEnd.text = formatDate(endDate)

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

        view.switchNotification.setOnCheckedChangeListener { buttonView, isChecked ->
            run {
                if (isChecked) {
                    showCreateNotificationDialogDialog()
                }
            }
        }

        createChannel(
            getString(R.string.notification_channel_id),
            getString(R.string.notification_channel_name)
        )

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
        val mMonth = now.month.ordinal
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

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(false)
                }
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "description"

            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun showCreateNotificationDialogDialog() {
        val dialog: DialogFragment = CreateNotificationDialogFragment()
        activity?.supportFragmentManager?.let { dialog.show(it, "CreateNotificationDialogFragment") }
    }

    override fun onDialogPositiveClick(dialogFragment: DialogFragment) {
        val radioGroup = dialogFragment.radioGroup
        radioGroup.checkedRadioButtonId
    }

}
