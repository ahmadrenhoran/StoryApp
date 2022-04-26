package com.shp.storyapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.shp.storyapp.data.model.LoginCredential
import com.shp.storyapp.data.model.User
import com.shp.storyapp.data.repository.SessionRepository
import com.shp.storyapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class LoginViewModel(private val sessionRepository: SessionRepository) : ViewModel() {

    fun login(loginCredential: LoginCredential) = sessionRepository.login(loginCredential)

}