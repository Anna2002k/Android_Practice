package com.example.android_practice.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.android_practice.MainActivity
import com.example.android_practice.R
import com.example.profile_feature.viewmodel.ClassStartNotificationReceiver

class AppNotificationReceiver : BroadcastReceiver() {

    companion object {
        private const val CHANNEL_ID = "class_start_notification"
        private const val NOTIFICATION_ID = 101
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ClassStartNotificationReceiver.OPEN_ACTIVITY_ACTION) {
            val title = intent.getStringExtra("title") ?: "Уведомление"
            val content = intent.getStringExtra("content") ?: ""
            showNotification(context, title, content)
        }
    }

    private fun showNotification(context: Context, title: String, content: String) {
        val mainIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Уведомления о начале пары"
            val descriptionText = "Уведомляет о начале любимой пары"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }


        with(NotificationManagerCompat.from(context)) {
            try {
                notify(NOTIFICATION_ID, builder.build())
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }
}