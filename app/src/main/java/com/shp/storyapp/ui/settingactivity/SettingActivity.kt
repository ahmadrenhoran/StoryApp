package com.shp.storyapp.ui.settingactivity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.shp.storyapp.data.repository.SessionRepository
import com.shp.storyapp.databinding.ActivitySettingBinding
import com.shp.storyapp.ui.ViewModelFactory
import com.shp.storyapp.ui.mainactivity.MainActivity
import java.lang.Exception
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingActivity : AppCompatActivity() {
    private val binding: ActivitySettingBinding by lazy {
        ActivitySettingBinding.inflate(layoutInflater)
    }
    private lateinit var settingViewModel: SettingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupViewModel()
        binding.currentLanguage.text = Locale.getDefault().displayLanguage
        setupAction()

    }

    private fun setupAction() {
        binding.languageSetting.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.btnLogout.setOnClickListener {



            try {
                Log.d("ADUDUD", "setupAction: ")
                settingViewModel.logout()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                Log.d("ADUDUD", "setupAction: ")
            } catch (exception: Exception) {
                Log.d("ADUDUD", "setupAction: ${exception.message.toString()}")
            }
        }
    }

    private fun setupViewModel() {
        settingViewModel = ViewModelProvider(
            this,
            ViewModelFactory(SessionRepository(applicationContext.dataStore))
        )[SettingViewModel::class.java]



    }


}