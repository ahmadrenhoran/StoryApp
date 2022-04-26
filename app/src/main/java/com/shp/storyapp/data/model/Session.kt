package com.shp.storyapp.data.model



data class Session(
    val userId: String,
    val name: String,
    val token: String,
    val isLogin: Boolean
)