package com.shp.storyapp.ui.settingactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shp.storyapp.data.repository.SessionRepository
import kotlinx.coroutines.launch

class SettingViewModel(private val sessionRepository: SessionRepository): ViewModel() {


    fun logout() {
        viewModelScope.launch {
            sessionRepository.logout()
        }
    }

}