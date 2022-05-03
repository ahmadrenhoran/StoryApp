package com.shp.storyapp.ui.storieslocation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.shp.storyapp.data.repository.StoryRepository

class StoryMapsViewModel : ViewModel() {
    fun getListStory(token: String) = StoryRepository.getInstance().getAllStoriesWithLocation(token)

    fun getToken() = StoryRepository.getInstance().getSessionPreference().getToken().asLiveData()
}