package com.shp.storyapp.data.remote.response

import com.google.gson.annotations.SerializedName
import com.shp.storyapp.data.model.DataStory

data class AllStoriesResponse(

	@field:SerializedName("listStory")
	val listStory: List<DataStory>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)


