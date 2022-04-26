package com.shp.storyapp.ui.mainactivity

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.shp.storyapp.R
import com.shp.storyapp.data.repository.SessionRepository
import com.shp.storyapp.databinding.ActivityMainBinding
import com.shp.storyapp.ui.ViewModelFactory
import com.shp.storyapp.ui.login.LoginFragment
import com.shp.storyapp.ui.register.RegisterFragment
import com.shp.storyapp.ui.storytimeline.StoryTimelineFragment

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupViewModel()
        setupStartFragment()




    }

    private fun setupStartFragment() {
        val mFragmentManager = supportFragmentManager

        mainViewModel.getSession().observe(this) { session ->
            Log.d("ADUDUD", "setupStartFragment: ${session.isLogin}")
            if (session.isLogin) {
                val storyTimelineFragment = StoryTimelineFragment()
                mFragmentManager
                    .beginTransaction()
                    .add(R.id.frame_container, storyTimelineFragment, StoryTimelineFragment::class.java.simpleName).commit()
            } else {
                val mLoginFragment = LoginFragment()
                mFragmentManager
                    .beginTransaction()
                    .add(R.id.frame_container, mLoginFragment, mLoginFragment::class.java.simpleName).commit()
            }
        }



    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(SessionRepository(applicationContext.dataStore))
        )[MainViewModel::class.java]


    }


}
