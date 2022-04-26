package com.shp.storyapp.ui.detailstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.shp.storyapp.data.model.DataStory
import com.shp.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private val binding: ActivityDetailBinding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupData()
    }

    private fun setupData() {
        val story = intent.getParcelableExtra<DataStory>(STORY_TAG) as DataStory
        Glide.with(applicationContext).load(story.photoUrl).into(binding.detailImageStory)
        binding.detailNamaStory.text = story.name
        binding.detailDeskripsiStory.text = story.description
        binding.detailDateStory.text = story.createdAt
    }

    companion object {
        const val STORY_TAG = "story"
    }
}