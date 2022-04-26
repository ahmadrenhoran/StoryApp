package com.shp.storyapp.ui.mainactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.shp.storyapp.data.model.Session
import com.shp.storyapp.data.repository.SessionRepository
import kotlinx.coroutines.flow.Flow

class MainViewModel(private val sessionRepository: SessionRepository) : ViewModel() {

    fun getSession(): LiveData<Session> = sessionRepository.getSessionPreference().getSession().asLiveData()

}