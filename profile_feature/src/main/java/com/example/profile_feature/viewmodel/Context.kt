package com.example.profile_feature.viewmodel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import java.util.Calendar

fun Context.scheduleNotification(fullName: String, time: String) {

    if (!isValidTimeFormat(time)) {
        return
    }
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (!alarmManager.canScheduleExactAlarms()) {
            Toast.makeText(this, "Необходимо разрешение для установки времени будильника", Toast.LENGTH_LONG).show()
            return
        }
    }

    val parts = time.split(":")
    val hour = parts[0].toInt()
    val minute = parts[1].toInt()

    val now = Calendar.getInstance()
    val notificationTime = Calendar.getInstance().apply {
        set(Calendar.YEAR, now.get(Calendar.YEAR))
        set(Calendar.MONTH, now.get(Calendar.MONTH))
        set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH))
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        if (timeInMillis <= now.timeInMillis) {
            add(Calendar.DAY_OF_MONTH, 1)
        }
    }

    val minDifference = 1 * 60 * 1000
    if (notificationTime.timeInMillis - now.timeInMillis < minDifference) {
        notificationTime.add(Calendar.MINUTE, 1)
    }

    val intent = Intent(this, ClassStartNotificationReceiver::class.java).apply {
        action = "com.example.profile_feature.CLASS_START_ALARM"
        putExtra("fullName", fullName)
    }

    val pendingIntent = PendingIntent.getBroadcast(
        this,
        ClassStartNotificationReceiver.NOTIFICATION_REQUEST_CODE,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    try {
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            notificationTime.timeInMillis,
            pendingIntent
        )
    } catch (e: SecurityException) {
        Toast.makeText(this, "Нет разрешения на установку точного времени будильника", Toast.LENGTH_LONG).show()
    }
}

private fun isValidTimeFormat(time: String): Boolean {
    return time.matches(Regex("^(0?[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$"))
}