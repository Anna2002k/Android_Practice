package com.example.profile_feature.di

import com.example.profile_feature.data.repository.ProfileRepository
import com.example.profile_feature.viewmodel.ProfileViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    single { ProfileRepository(androidContext()) }
    viewModel { ProfileViewModel(get()) }
}