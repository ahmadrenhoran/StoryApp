package com.shp.storyapp.ui.addstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.shp.storyapp.R
import com.shp.storyapp.databinding.ActivityAddStoryBinding
import com.shp.storyapp.ui.cameraactivity.CameraActivity
import com.shp.storyapp.ui.mainactivity.MainViewModel
import com.shp.storyapp.utils.Resource
import com.shp.storyapp.utils.reduceFileImage
import com.shp.storyapp.utils.rotateBitmap
import com.shp.storyapp.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private val binding: ActivityAddStoryBinding by lazy {
        ActivityAddStoryBinding.inflate(
            layoutInflater
        )
    }
    private lateinit var addStoryViewModel: AddStoryViewModel
    private var getFile: File? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat: Double? = null
    private var lon: Double? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.rejected_permission),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setupPermission()
        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        addStoryViewModel = ViewModelProvider(this)[AddStoryViewModel::class.java]
    }

    private fun setupAction() {
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener { uploadStory() }
    }

    private fun uploadStory() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val description = binding.etDescription.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            addStoryViewModel.getToken().observe(this) {
                addStoryViewModel.uploadStory(it, imageMultipart, description, lat, lon).observe(this) { resource ->
                    when(resource) {
                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.addStoryLayout.visibility = View.GONE
                        }
                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.addStoryLayout.visibility = View.VISIBLE
                            Toast.makeText(this@AddStoryActivity, resource.data.message, Toast.LENGTH_LONG).show()
                            finish()
                        }
                        is Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.addStoryLayout.visibility = View.GONE
                            Toast.makeText(this@AddStoryActivity, resource.error, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }

        } else {
            Toast.makeText(this@AddStoryActivity, resources.getString(R.string.insert_image), Toast.LENGTH_SHORT).show()
            Log.d("ADD", "setupViewModel: error")
        }

    }



    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra(CameraActivity.PICTURE_RESULT) as File
            val isBackCamera =
                it.data?.getBooleanExtra(CameraActivity.IS_BACK_CAMERA, true) as Boolean
            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )
            getFile = myFile
            binding.capturedImage.setImageBitmap(result)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, resources.getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoryActivity)
            getFile = myFile
            binding.capturedImage.setImageURI(selectedImg)
        }
    }


    private fun setupPermission() {
        getMyLastLocation()
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                else -> {
                    // do nothing.
                }
            }
        }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    lat = location.latitude
                    lon = location.latitude
                }

            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }


    companion object {
        const val CAMERA_X_RESULT = 20
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
        const val REQUEST_CODE_PERMISSIONS = 10
    }
}