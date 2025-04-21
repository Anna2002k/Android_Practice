package com.example.android_practice

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.util.Calendar
import java.util.Locale

fun Context.scheduleNotification(fullName: String, time: String) {
    if (!isValidTimeFormat(time)) return

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
    }

    if (notificationTime.timeInMillis <= now.timeInMillis) {
        notificationTime.add(Calendar.DAY_OF_MONTH, 1)
    }

    val intent = Intent("com.example.android_practice.CLASS_START_ALARM").apply {
        putExtra("fullName", fullName)
        `package` = packageName
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
    return time.matches(Regex("^\\d{2}:\\d{2}$"))
}