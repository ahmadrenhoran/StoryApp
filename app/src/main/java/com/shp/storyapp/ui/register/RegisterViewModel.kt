package com.shp.storyapp.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.shp.storyapp.data.model.User
import com.shp.storyapp.data.repository.SessionRepository
import com.shp.storyapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class RegisterViewModel (private val sessionRepository: SessionRepository): ViewModel(){

    fun register(user: User) = sessionRepository.register(user)
}