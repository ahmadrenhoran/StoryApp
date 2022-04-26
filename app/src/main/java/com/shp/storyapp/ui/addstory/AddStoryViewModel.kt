package com.shp.storyapp.ui.addstory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.shp.storyapp.data.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

class AddStoryViewModel : ViewModel() {

    fun uploadStory(
        token: String, @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ) = StoryRepository.getInstance().uploadStory(token, file, description)

    fun getToken() = StoryRepository.getInstance().getSessionPreference().getToken().asLiveData()
}