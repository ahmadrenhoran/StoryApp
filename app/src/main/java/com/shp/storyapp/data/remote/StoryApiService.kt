package com.shp.storyapp.data.remote

import com.shp.storyapp.data.model.LoginCredential
import com.shp.storyapp.data.model.User
import com.shp.storyapp.data.remote.response.AllStoriesResponse
import com.shp.storyapp.data.remote.response.LoginResponse
import com.shp.storyapp.data.remote.response.NewStoryResponse
import com.shp.storyapp.data.remote.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface StoryApiService {

    @POST("register")
    suspend fun register(@Body user: User): RegisterResponse

    @POST("login")
    suspend fun login(@Body loginCredential: LoginCredential): LoginResponse

    @GET("stories")
    suspend fun getAllStories(@Header("Authorization") token: String): AllStoriesResponse


    @Multipart
    @POST("stories")
    suspend fun addNewStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): NewStoryResponse

}