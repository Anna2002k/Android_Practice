package com.example.android_practice.data.repository

import android.content.Context
import com.example.android_practice.data.Profile
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
            apply()
        }
        _profileFlow.value = profile
    }

    fun getProfile(): Profile {
        return Profile(
            fullName = sharedPrefs.getString("fullName", "") ?: "",
            avatarUri = sharedPrefs.getString("avatarUri", null),
            resumeUrl = sharedPrefs.getString("resumeUrl", "") ?: ""
        )
    }
}