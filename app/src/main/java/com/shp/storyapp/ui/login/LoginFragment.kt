package com.shp.storyapp.ui.login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.shp.storyapp.R
import com.shp.storyapp.data.model.LoginCredential
import com.shp.storyapp.data.repository.SessionRepository
import com.shp.storyapp.databinding.FragmentLoginBinding
import com.shp.storyapp.ui.ViewModelFactory
import com.shp.storyapp.ui.register.RegisterFragment
import com.shp.storyapp.ui.storytimeline.StoryTimelineFragment
import com.shp.storyapp.utils.Resource

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var logViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupAction()


    }



    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val loginCredential = LoginCredential(binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())
            logViewModel.login(loginCredential).observe(viewLifecycleOwner) { resource ->
                if (resource != null) {
                    when (resource) {
                        is Resource.Loading -> {
                            binding.fragmentLoginLayout.visibility = View.GONE
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            Toast.makeText(requireContext(), resource.data.message, Toast.LENGTH_LONG).show()
                            val mStoryTimelineFragment = StoryTimelineFragment()
                            val parentFragmentManager = this@LoginFragment.parentFragmentManager
                            parentFragmentManager.beginTransaction().apply {
                                replace(R.id.frame_container, mStoryTimelineFragment, StoryTimelineFragment::class.java.simpleName)
                                commit()
                            }
                        }
                        is Resource.Error -> {
                            binding.fragmentLoginLayout.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(requireContext(), resource.error, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        binding.tvSignUp.setOnClickListener {
            val mRegisterFragment = RegisterFragment()
            val parentFragmentManager = this@LoginFragment.parentFragmentManager
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.frame_container, mRegisterFragment, RegisterFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun setupViewModel() {
        logViewModel = ViewModelProvider(
            this,
            ViewModelFactory(SessionRepository.getInstance(requireContext().dataStore))
        )[LoginViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}