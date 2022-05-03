package com.shp.storyapp.ui.storieslocation

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.shp.storyapp.R
import com.shp.storyapp.data.model.DataStory
import com.shp.storyapp.databinding.ActivityStoryMapBinding
import com.shp.storyapp.ui.ViewModelFactory
import com.shp.storyapp.ui.storytimeline.StoryTimelineViewModel
import com.shp.storyapp.utils.Resource

class StoryMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private val binding by lazy {
        ActivityStoryMapBinding.inflate(layoutInflater)
    }
    private lateinit var mMap: GoogleMap
    private lateinit var storyMapsViewModel: StoryMapsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupViewModel()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupData() {
        storyMapsViewModel.getToken().observe(this) { token ->
            storyMapsViewModel.getListStory(token).observe(this) { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        if (resource.data.listStory.isEmpty()) {
                            Toast.makeText(
                                this@StoryMapActivity,
                                resources.getString(R.string.empty_data),
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            setMarker(resource.data.listStory)
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Log.d("BAUD", "setupViewModel: $resource.error")
                        Toast.makeText(this@StoryMapActivity, resource.error, Toast.LENGTH_LONG)
                            .show()
                    }
                }

            }
        }
    }

    override fun onMapReady(myMap: GoogleMap) {
        mMap = myMap
        setMapStyle()
        setMapSetting()
        setupData()
    }

    private fun setMarker(listDataStory: List<DataStory>) {
        for(data in listDataStory) {
            val lat: Double = data.lat
            val lon: Double = data.lon
            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(lat, lon))
                    .title(data.name)
                    .snippet(data.description)
            )
        }
    }


    private fun setupViewModel() {
        storyMapsViewModel = ViewModelProvider(this)[StoryMapsViewModel::class.java]
    }


    private fun setMapSetting() {
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }


    companion object {
        const val TAG = "StoryMapActivity"
    }
}