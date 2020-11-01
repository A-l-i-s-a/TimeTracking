package com.example.timetracking.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "notification_table")
data class Notification(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    @ColumnInfo(name = "notify_for")
    var notifyFor: NotifyFor,
    @ColumnInfo(name = "count")
    var count: Int
) : Parcelable