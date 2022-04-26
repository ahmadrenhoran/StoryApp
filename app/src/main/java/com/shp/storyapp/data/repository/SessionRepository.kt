package com.shp.storyapp.data.repository


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.shp.storyapp.data.model.LoginCredential
import com.shp.storyapp.data.model.Session
import com.shp.storyapp.data.model.SessionPreference
import com.shp.storyapp.data.model.User
import com.shp.storyapp.data.remote.response.LoginResponse
import com.shp.storyapp.data.remote.response.RegisterResponse
import com.shp.storyapp.di.AppModule
import com.shp.storyapp.utils.Resource
import java.lang.Exception




class SessionRepository(private var dataStore: DataStore<Preferences>) {



    fun getSessionPreference() = SessionPreference.getInstance(dataStore)

    fun login(loginCredential: LoginCredential): LiveData<Resource<LoginResponse>> = liveData {
        emit(Resource.Loading)
        try {
            emit(
                Resource.Success(
                    AppModule.getStoryApiService().login(loginCredential).also { response ->
                        getSessionPreference().saveSession(
                            Session(
                                response.loginResult.userId,
                                response.loginResult.name,
                                response.loginResult.token,
                                true
                            )
                        )
                    })
            )

        } catch (exception: Exception) {
            emit(Resource.Error(exception.message.toString()))
        }
    }


    fun register(user: User): LiveData<Resource<RegisterResponse>> = liveData {
        emit(Resource.Loading)
        try {
            emit(Resource.Success(AppModule.getStoryApiService().register(user)))
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message.toString()))
        }
    }

    suspend fun logout() {
        getSessionPreference().logout()
    }

    companion object {
        @Volatile
        private var INSTANCE: SessionRepository? = null

        fun getInstance(dataStore: DataStore<Preferences>): SessionRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionRepository(dataStore)

                INSTANCE = instance
                instance
            }
        }

        fun getInstance() = INSTANCE


    }
}