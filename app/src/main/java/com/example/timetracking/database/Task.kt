package com.example.timetracking.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*
@Parcelize
@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    @ColumnInfo(name = "headline")
    val headline: String,
    @ColumnInfo(name = "time_beginning")
    val timeBeginning: String = Calendar.getInstance().toString(),
    @ColumnInfo(name = "time_end")
    val timeEnd: String = Calendar.getInstance().toString(),
    @ColumnInfo(name = "place")
    val place: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "todo_is_done")
    var todoIsDone: Boolean = false
) : Parcelable