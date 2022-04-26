package com.shp.storyapp.ui.storytimeline

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shp.storyapp.data.model.DataStory
import com.shp.storyapp.databinding.StoryItemBinding
import com.shp.storyapp.ui.detailstory.DetailActivity

class StoryTimelineAdapter(private val listStory: List<DataStory>) :
    RecyclerView.Adapter<StoryTimelineAdapter.ViewHolder>() {

    class ViewHolder (var binding: StoryItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(StoryItemBinding.inflate(
        LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story: DataStory = listStory[position]
        holder.apply {
            Glide.with(itemView.context).load(story.photoUrl).into(binding.storyImage)
            binding.storyUserName.text = story.name
            binding.storyUploadDate.text = story.createdAt

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.STORY_TAG, story)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.storyImage, "story_image"),
                        Pair(binding.storyUserName, "user_name"),
                        Pair(binding.storyUploadDate, "upload_date"),
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }

    }

    override fun getItemCount(): Int = listStory.size
}