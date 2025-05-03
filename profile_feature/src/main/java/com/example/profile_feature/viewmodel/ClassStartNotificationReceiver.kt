package com.example.profile_feature.viewmodel

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.content.ContextCompat

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

            "com.example.profile_feature.CLASS_START_ALARM" -> {
                val fullName = intent.getStringExtra("fullName") ?: "Студент"
                val hasPermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED

                if (hasPermission) {
                    sendNotificationIntentToApp(context, fullName)
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

    private fun sendNotificationIntentToApp(context: Context, fullName: String) {
        val notificationIntent = Intent("com.example.profile_feature.OPEN_ACTIVITY_ACTION").apply {
            putExtra("title", "Начало пары!")
            putExtra("content", "$fullName, ваша любимая пара началась.")
            setPackage(context.packageName)
        }
        context.sendBroadcast(notificationIntent)
    }


    companion object {
        private const val CHANNEL_ID = "class_start_notification"
        private const val NOTIFICATION_ID = 101
        const val NOTIFICATION_REQUEST_CODE = 123
        const val CLASS_START_ALARM = "com.example.profile_feature.CLASS_START_ALARM"
        const val OPEN_ACTIVITY_ACTION = "com.example.profile_feature.OPEN_ACTIVITY_ACTION"
    }

}