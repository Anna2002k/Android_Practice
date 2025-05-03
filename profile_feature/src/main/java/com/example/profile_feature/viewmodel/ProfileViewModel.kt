package com.example.profile_feature.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.profile_feature.data.repository.ProfileRepository
import com.example.profile_feature.data.Profile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {
    private val _profileState = MutableStateFlow(repository.getProfile())
    val profileState: StateFlow<Profile> = repository.profileFlow

    fun updateProfile(profile: Profile) {
        viewModelScope.launch {
            repository.saveProfile(profile)
            _profileState.value = profile;
        }
    }

    fun updateAvatar(uri: String?) {
        viewModelScope.launch {
            val currentProfile = _profileState.value.copy(avatarUri = uri)
            repository.saveProfile(currentProfile)
            _profileState.value = currentProfile
        }
    }

    fun clearScheduledNotification(context: Context) {
        repository.clearScheduledNotification(context)
    }
}