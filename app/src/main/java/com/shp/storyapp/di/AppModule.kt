package com.shp.storyapp.di


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.shp.storyapp.BuildConfig
import com.shp.storyapp.data.remote.StoryApiService
import com.shp.storyapp.data.repository.SessionRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppModule {

    fun getStoryApiService(): StoryApiService {
        val loggingInterceptor = when (BuildConfig.DEBUG) {
            true -> HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            false -> HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }



        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()


        val retrofit = Retrofit.Builder().client(client).addConverterFactory(GsonConverterFactory.create()).baseUrl(BuildConfig.BASE_URL).build()

        return  retrofit.create(StoryApiService::class.java)

    }


}