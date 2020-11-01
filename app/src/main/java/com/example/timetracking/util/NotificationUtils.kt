package com.example.timetracking.util

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.timetracking.R

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context, idTask: Long) {
    val builder =  NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.notification_channel_id)
    )
        .setSmallIcon(R.drawable.icon)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)

    notify(idTask.toInt(), builder.build())
}

class NotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val text = intent.getStringExtra("messageBody")
        val intExtra = intent.getLongExtra("idTask", 0)
        text?.let { notificationManager.sendNotification(it, context, intExtra) }
    }

    fun scheduleNotification(context: Context, time: Long, messageBody: String, idTask: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, getPendingIntent(context, messageBody, idTask))
    }

    fun cancelNotification(context: Context, messageBody: String, idTask: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(getPendingIntent(context, messageBody, idTask))
    }

    private fun getPendingIntent(context: Context, messageBody: String, idTask: Long): PendingIntent {
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra("messageBody", messageBody)
        intent.putExtra("idTask", idTask)
        return PendingIntent.getBroadcast(
            context,
            messageBody.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

}
