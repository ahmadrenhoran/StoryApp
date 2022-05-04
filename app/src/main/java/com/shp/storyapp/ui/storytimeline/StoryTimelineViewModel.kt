package com.shp.storyapp.ui.storytimeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.shp.storyapp.data.model.DataStory
import com.shp.storyapp.data.repository.StoryRepository

class StoryTimelineViewModel : ViewModel() {

    fun getStories (token: String): LiveData<PagingData<DataStory>> =
        StoryRepository.getInstance().getStories(token).cachedIn(viewModelScope)

    fun getListStory(token: String) = StoryRepository.getInstance().getAllStories(token)

    fun getToken() = StoryRepository.getInstance().getSessionPreference().getToken().asLiveData()
}