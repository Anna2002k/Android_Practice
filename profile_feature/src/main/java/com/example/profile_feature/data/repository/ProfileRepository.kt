package com.example.profile_feature.data.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.profile_feature.data.Profile
import com.example.profile_feature.viewmodel.ClassStartNotificationReceiver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileRepository(private val context: Context) {
    private val sharedPrefs = context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
    private val _profileFlow = MutableStateFlow(getProfile())
    val profileFlow: StateFlow<Profile> = _profileFlow

    fun saveProfile(profile: Profile) {
        sharedPrefs.edit().apply {
            putString("fullName", profile.fullName)
            putString("avatarUri", profile.avatarUri)
            putString("resumeUrl", profile.resumeUrl)
            putString("favoriteClassTime", profile.favoriteClassTime)
            apply()
        }
        _profileFlow.value = profile
    }

    fun getProfile(): Profile {
        return Profile(
            fullName = sharedPrefs.getString("fullName", "") ?: "",
            avatarUri = sharedPrefs.getString("avatarUri", null),
            resumeUrl = sharedPrefs.getString("resumeUrl", "") ?: "",
            favoriteClassTime = sharedPrefs.getString("favoriteClassTime", "") ?: ""
        )
    }

    fun clearScheduledNotification(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ClassStartNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ClassStartNotificationReceiver.NOTIFICATION_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        pendingIntent?.cancel()
    }
}