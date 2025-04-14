package com.example.android_practice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_practice.data.Profile
import com.example.android_practice.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
}