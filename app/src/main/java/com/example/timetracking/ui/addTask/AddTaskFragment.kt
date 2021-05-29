package com.example.timetracking.ui.addTask

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timetracking.R
import com.example.timetracking.database.Task
import com.example.timetracking.ui.AttachAdapter
import com.example.timetracking.ui.dialog.CreateNotificationDialogFragment
import com.example.timetracking.util.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.add_task_fragment.*
import kotlinx.android.synthetic.main.add_task_fragment.view.*
import kotlinx.android.synthetic.main.choice_upload.*
import kotlinx.android.synthetic.main.create_notification.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import java.io.File
import java.lang.IllegalArgumentException
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class AddTaskFragment : Fragment(),
    CreateNotificationDialogFragment.CreateNotificationDialogListener {

    companion object {
        fun newInstance() = AddTaskFragment()
    }

    private val viewModel: AddTaskViewModel by viewModels()
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<*>
    private val attach: MutableList<Uri> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_task_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribeObservers()

        mBottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_choice)
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        btn_attach.setOnClickListener { attach() }

        attachRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        attachRecyclerView.adapter = AttachAdapter(attach, object : AttachAdapter.Listener {
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

        var beginningDate: LocalDate = LocalDate.now()
        var endDate: LocalDate = LocalDate.now()
        var beginningTime: LocalTime = LocalTime.now()
        var endTime: LocalTime = LocalTime.now()

        inputTimeBeginning.text = formatTime(beginningTime)
        inputTimeEnd.text = formatTime(endTime)

        inputDateBeginning.text = formatDate(beginningDate)
        inputDateEnd.text = formatDate(endDate)

        inputDateBeginning.setOnClickListener {
            onClickDate { date ->
                run {
                    beginningDate = date
                    inputDateBeginning.text = formatDate(beginningDate)
                }
            }
        }

        inputDateEnd.setOnClickListener {
            onClickDate { date ->
                run {
                    endDate = date
                    inputDateEnd.text = formatDate(endDate)
                }
            }
        }

        inputTimeBeginning.setOnClickListener {
            onClickTime { time ->
                run {
                    beginningTime = time
                    inputTimeBeginning.text = formatTime(beginningTime)
                }
            }
        }

        inputTimeEnd.setOnClickListener {
            onClickTime { time ->
                run {
                    endTime = time
                    inputTimeEnd.text = formatTime(endTime)
                }
            }
        }

        buttonCreateTask.setOnClickListener {
            viewModel.createTask(
                Task(
                    headline = editTextHeadline.text.toString(),
                    place = editTextPlace.text.toString(),
                    description = editTextDescription.text.toString(),
                    timeBeginning = Timestamp(LocalTime.now().toNanoOfDay()),
                    timeEnd = Timestamp(LocalTime.now().toNanoOfDay())
                )
            )
            Timber.i("click on button 'Create'")
            this.findNavController().popBackStack()
        }

        switchNotification.setOnCheckedChangeListener { buttonView, isChecked ->
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
    }

    private fun attach() {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        btn_attach_file.setOnClickListener { attachFile() }
        btn_attach_image.setOnClickListener { attachImage() }
    }

    private fun attachFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
    }

    private fun attachImage() {
        CropImage.activity()
            .start(APP_ACTIVITY, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val activityResult = CropImage.getActivityResult(data)
                    activityResult.uri.lastPathSegment?.let {
                        attach.add(activityResult.uri)
                    }
                }
                PICK_FILE_REQUEST_CODE -> {
                    val uri = data.data
                    uri?.let {
                        attach.add(getUriFile(uri))
                    }
                }
            }
        }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is DataState.Success<Task> -> {
                    displayProgressBar(false)
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

    private fun getNewTask(): Task {
        var calendar: Calendar = Calendar.getInstance()
        val bundle: Bundle? = arguments
        if (bundle != null) {
            calendar = bundle.get("date") as Calendar
        }

        val dateStart: Timestamp? = Timestamp(LocalDate.now().toEpochDay())
//            Timestamp(calendar.timeInMillis + timeStrToMillis(editTextAddStartTime.text.toString())) // editTextStartTime
        val dateFinish: Timestamp? = Timestamp(LocalDate.now().toEpochDay())
//            Timestamp(calendar.timeInMillis + timeStrToMillis(editTextAddFinishTime.text.toString())) // editTextFinishTime
        val name: String = "name" //editTextTitle.text.toString()
        val description: String = editTextDescription.text.toString()
        return Task(
            timeBeginning = dateStart,
            timeEnd = dateFinish,
            headline = name,
            description = description,
            attachments = attach
        )
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
        activity?.supportFragmentManager?.let {
            dialog.show(
                it,
                "CreateNotificationDialogFragment"
            )
        }
    }

    override fun onDialogPositiveClick(dialogFragment: DialogFragment) {
        val radioGroup = dialogFragment.radioGroup
        radioGroup.checkedRadioButtonId
    }

}
