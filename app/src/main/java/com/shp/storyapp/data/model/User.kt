package com.shp.storyapp.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    private val email: String,
    @SerializedName("password")
    private val password: String
)
