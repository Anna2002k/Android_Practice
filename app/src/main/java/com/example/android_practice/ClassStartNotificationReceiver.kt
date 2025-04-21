package com.example.android_practice

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import java.util.Calendar

class ClassStartNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "android.intent.action.BOOT_COMPLETED" -> {
                val sharedPrefs =
                    context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
                val fullName = sharedPrefs.getString("fullName", "") ?: ""
                val favoriteClassTime = sharedPrefs.getString("favoriteClassTime", "") ?: ""

                if (favoriteClassTime.isNotEmpty()) {
                    context.scheduleNotification(fullName, favoriteClassTime)
                }
            }

            "com.example.android_practice.CLASS_START_ALARM" -> {
                val fullName = intent.getStringExtra("fullName") ?: "Студент"
                createNotificationChannel(context)
                val hasPermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED

                if (hasPermission) {
                    showNotification(context, fullName)
                } else {
                    Toast.makeText(
                        context,
                        "Разрешение на уведомления не предоставлено",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        }
    }

  /*  private fun scheduleNotification(context: Context, fullName: String, time: String) {
        if (!isValidTimeFormat(time)) return

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

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent("com.example.android_practice.CLASS_START_ALARM").apply {
            putExtra("fullName", fullName)
            `package` = context.packageName
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            NOTIFICATION_REQUEST_CODE,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            notificationTime.timeInMillis,
            pendingIntent
        )
    }*/

    private fun isValidTimeFormat(time: String): Boolean {
        return time.matches(Regex("^\\d{2}:\\d{2}$"))
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Уведомления о начале пары"
            val descriptionText = "Уведомляет о начале любимой пары"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(context: Context, fullName: String) {

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Начало пары!")
            .setContentText("$fullName, ваша любимая пара началась.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            try {
                notify(NOTIFICATION_ID, builder.build())
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private const val CHANNEL_ID = "class_start_notification"
        private const val NOTIFICATION_ID = 101
        const val NOTIFICATION_REQUEST_CODE = 123
    }
}