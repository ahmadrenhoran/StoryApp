package com.shp.storyapp.data.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.shp.storyapp.data.model.DataStory
import com.shp.storyapp.data.model.SessionPreference
import com.shp.storyapp.data.remote.StoriesPagingSource
import com.shp.storyapp.data.remote.response.AllStoriesResponse
import com.shp.storyapp.data.remote.response.NewStoryResponse
import com.shp.storyapp.di.AppModule
import com.shp.storyapp.utils.Resource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import java.lang.Exception

class StoryRepository {

    fun getSessionPreference() = SessionPreference.getInstance()

    fun getStories(token: String): LiveData<PagingData<DataStory>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoriesPagingSource(AppModule.getStoryApiService(), token)
            }
        ).liveData
    }
    fun getAllStories(token: String): LiveData<Resource<AllStoriesResponse>> = liveData {
        emit(Resource.Loading)
        try {
            emit(Resource.Success(AppModule.getStoryApiService().getAllStories("Bearer $token")))
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message.toString()))
        }
    }

    fun getAllStoriesWithLocation(token: String): LiveData<Resource<AllStoriesResponse>> = liveData {
        emit(Resource.Loading)
        try {
            emit(Resource.Success(AppModule.getStoryApiService().getAllStoriesWithLocation("Bearer $token")))
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message.toString()))
        }
    }

    fun uploadStory(
        token: String, @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Double?,
        @Part("lon") lon: Double?
    ): LiveData<Resource<NewStoryResponse>> = liveData {
        emit(Resource.Loading)
        try {
            emit(Resource.Success(AppModule.getStoryApiService().addNewStory("Bearer $token", file, description, lat, lon)))
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message.toString()))
        }
    }



    companion object {
        @Volatile
        private var INSTANCE: StoryRepository? = null

        fun getInstance(): StoryRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = StoryRepository()
                INSTANCE = instance
                instance
            }
        }
    }
}