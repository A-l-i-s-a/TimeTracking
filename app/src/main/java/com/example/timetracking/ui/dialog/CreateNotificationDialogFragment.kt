package com.example.timetracking.ui.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.timetracking.R
import java.lang.ClassCastException

class CreateNotificationDialogFragment : DialogFragment() {

    interface CreateNotificationDialogListener {
        fun onDialogPositiveClick(dialogFragment: DialogFragment)
    }

    lateinit var mListener: CreateNotificationDialogListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as CreateNotificationDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString()
                    + " must implement CreateNotificationDialogListener")
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val layoutInflater = activity!!.layoutInflater
        builder.setView(layoutInflater.inflate(R.layout.create_notification, null))
            .setPositiveButton(R.string.ok) { dialog, which ->
                mListener.onDialogPositiveClick(this)
            }
        return builder.create()
    }
}