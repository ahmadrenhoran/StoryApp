package com.shp.storyapp.ui.storytimeline

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.shp.storyapp.R
import com.shp.storyapp.data.model.DataStory
import com.shp.storyapp.databinding.FragmentStoryTimelineBinding
import com.shp.storyapp.ui.addstory.AddStoryActivity
import com.shp.storyapp.ui.settingactivity.SettingActivity
import com.shp.storyapp.ui.storieslocation.StoryMapActivity
import com.shp.storyapp.utils.Resource

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
        storyTLviewModel.getToken().observe(viewLifecycleOwner) { token ->
            Log.d("BAUD", "setupViewModel: $token")
            storyTLviewModel.getListStory(token).observe(viewLifecycleOwner) { resource ->
                when(resource) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.rvListStory.visibility = View.GONE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.rvListStory.visibility = View.VISIBLE
                        if (resource.data.listStory.isEmpty()) {
                            Toast.makeText(requireContext(), resources.getString(R.string.empty_data), Toast.LENGTH_LONG).show()
                        } else {
                            setupList(resource.data.listStory)
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.rvListStory.visibility = View.GONE
                        Log.d("BAUD", "setupViewModel: $resource.error")
                        Toast.makeText(requireContext(), resource.error, Toast.LENGTH_LONG).show()
                    }
                }

            }
        }
    }

    private fun setupList(list: List<DataStory>) {
        with(binding.rvListStory) {
            adapter = StoryTimelineAdapter(list)
            setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }







}