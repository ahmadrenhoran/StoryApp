package com.shp.storyapp.ui.storytimeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.shp.storyapp.data.repository.StoryRepository

class StoryTimelineViewModel : ViewModel() {

    fun getListStory(token: String) = StoryRepository.getInstance().getAllStories(token)

    fun getToken() = StoryRepository.getInstance().getSessionPreference().getToken().asLiveData()
}