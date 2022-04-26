package com.shp.storyapp.ui.register

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.shp.storyapp.R
import com.shp.storyapp.data.model.User
import com.shp.storyapp.data.repository.SessionRepository
import com.shp.storyapp.databinding.FragmentRegisterBinding
import com.shp.storyapp.ui.ViewModelFactory
import com.shp.storyapp.ui.mainactivity.MainViewModel
import com.shp.storyapp.utils.Resource
import java.lang.Exception
import java.security.cert.CertStoreSpi

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var regViewModel: RegisterViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupAction()

    }

    private fun setupViewModel() {
        regViewModel = ViewModelProvider(
            this,
            ViewModelFactory(SessionRepository.getInstance(requireContext().dataStore))
        )[RegisterViewModel::class.java]
    }

    private fun setupAction() {

        binding.registerButton.setOnClickListener {
            val user = User(binding.nameEditText.text.toString(), binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())

            regViewModel.register(user).observe(viewLifecycleOwner) { resource ->
                if (resource != null) {
                    when (resource) {
                        is Resource.Loading -> {
                            binding.registerLayout.visibility = View.GONE
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            binding.registerLayout.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(requireContext(), resource.data.message, Toast.LENGTH_LONG).show()
                        }
                        is Resource.Error -> {
                            binding.registerLayout.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(requireContext(), resource.error, Toast.LENGTH_LONG).show()
                        }

                    }
                }

            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}