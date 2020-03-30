package com.example.timetracking.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    @ColumnInfo(name = "headline")
    val headline: String,
    @ColumnInfo(name = "time_beginning")
    val timeBeginning: Calendar,
    @ColumnInfo(name = "time_end")
    val timeEnd: Calendar,
    @ColumnInfo(name = "place")
    val place: String,
    @ColumnInfo(name = "description")
    val description: String
)