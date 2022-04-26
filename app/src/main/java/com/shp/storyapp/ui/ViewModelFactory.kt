package com.shp.storyapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shp.storyapp.data.repository.SessionRepository
import com.shp.storyapp.ui.login.LoginViewModel
import com.shp.storyapp.ui.mainactivity.MainViewModel
import com.shp.storyapp.ui.register.RegisterViewModel
import com.shp.storyapp.ui.settingactivity.SettingViewModel

class ViewModelFactory(private val sessionRepository: SessionRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(sessionRepository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(sessionRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(sessionRepository) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(sessionRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}