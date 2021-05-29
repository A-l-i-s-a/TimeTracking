package com.example.timetracking.database

import android.net.Uri
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.sql.Timestamp
import java.time.OffsetDateTime

@Parcelize
data class Task(
    var id: Long = 0L,
    val headline: String = "New Task",
    val timeBeginning: Timestamp? = null,
    val timeEnd: Timestamp? = null,
    val place: String = "",
    val description: String = "",
    var todoIsDone: Boolean = false,
    var notification: Notification? = null,
    var attachments: List<Uri> = listOf(),
    var isNeedSynchronization: Boolean = false
) : Parcelable