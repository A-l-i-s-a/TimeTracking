package com.example.timetracking.repository.local_data_source.room

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "task_table")
data class TaskCacheEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    @ColumnInfo(name = "headline")
    val headline: String = "New Task",
    @ColumnInfo(name = "time_beginning")
    val timeBeginning: Long? = null,
    @ColumnInfo(name = "time_end")
    val timeEnd: Long? = null,
    @ColumnInfo(name = "place")
    val place: String = "",
    @ColumnInfo(name = "description")
    val description: String = "",
    @ColumnInfo(name = "todo_is_done")
    var todoIsDone: Boolean = false,
    @ColumnInfo(name = "notification")
    var notificationId: Long? = null,
    @ColumnInfo(name = "attachments")
    var attachments: List<String> = listOf(),
    @ColumnInfo(name = "is_need_synchronization")
    var isNeedSynchronization: Boolean = false
) : Parcelable