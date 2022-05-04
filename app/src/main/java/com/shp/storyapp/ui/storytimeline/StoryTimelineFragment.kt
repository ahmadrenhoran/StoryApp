package com.shp.storyapp.ui.storytimeline

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.shp.storyapp.databinding.FragmentStoryTimelineBinding
import com.shp.storyapp.ui.addstory.AddStoryActivity
import com.shp.storyapp.ui.settingactivity.SettingActivity
import com.shp.storyapp.ui.storieslocation.StoryMapActivity

class StoryTimelineFragment : Fragment() {
    private var _binding: FragmentStoryTimelineBinding? = null
    private val binding get() = _binding!!
    private lateinit var storyTLviewModel: StoryTimelineViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryTimelineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        getData()
        setupAction()
    }

    private fun setupAction() {
        binding.fabAddStory.setOnClickListener {
            val intent = Intent(requireContext(), AddStoryActivity::class.java)
            startActivity(intent)
        }

        binding.toolbarTitle.setOnClickListener {
            val intent = Intent(requireContext(), SettingActivity::class.java)
            startActivity(intent)
        }

        binding.toolbarMap.setOnClickListener {
            val intent = Intent(requireContext(), StoryMapActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupViewModel() {
        storyTLviewModel = ViewModelProvider(this)[StoryTimelineViewModel::class.java]
    }

    private fun getData() {
        val adapter = StoryTimelineAdapter()
        binding.rvListStory.adapter = adapter
        storyTLviewModel.getToken().observe(viewLifecycleOwner) { token ->
            storyTLviewModel.getStories(token).observe(viewLifecycleOwner) {

                adapter.submitData(lifecycle, it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }







}