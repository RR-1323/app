package com.example.myapplication.ui.main.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAuthBinding
import com.example.myapplication.databinding.FragmentDetailBinding


import com.example.myapplication.ui.main.detail.DetailViewModel
import com.example.myapplication.ui.main.state.LoadState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


var authcode: String = ""
var accessToken: String = ""

class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<AuthFragmentArgs>()
    private val viewModel: AuthViewModel by viewModels()


    private val authorizeLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val intentData = it.data ?: return@registerForActivityResult
            Log.d("AAA intentData", intentData.toString())
            viewModel.handleAuthResponseIntent(intentData)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingObserve()
        tokenObserve(createSharedPreference(TOKEN_SHARED_NAME))
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authSuccessEventChannel.collect {
                    //  findNavController().navigate(R.id.action_authFragment_to_mainFragment)
                    findNavController().navigate(R.id.action_authFragment_to_profileFragment)
                    //  startActivity(Intent(requireContext(), MainActivity::class.java))
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.toastEventChannel.collect {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getText(it),
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_authFragment_to_profileFragment)
                    // findNavController().navigate(R.id.action_authFragment_to_mainFragment)
                }
            }
        }
        binding.btnAuth.setOnClickListener {
            // viewModel.openLoginPage()
            openAuthPage()
        }

    }

    fun createSharedPreference(sharedName: String) =
        requireContext().getSharedPreferences(sharedName, Context.MODE_PRIVATE)

    private fun tokenObserve(preferences: SharedPreferences) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.token.collect { token ->
                preferences.edit().putString(TOKEN_SHARED_KEY, token).apply()
                preferences.edit().putBoolean(TOKEN_ENABLED_KEY, true).apply()

                Log.d(
                    "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------AuthFragment",
                    token
                )
            }
        }
    }

    private fun openAuthPage() {
        viewModel.prepareAuthPageIntent { authorizeLauncher.launch(it) }
    }


    private fun loadingObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadState.collect { loadState ->
                when (loadState) {
                    LoadState.START ->
                        setLoadState(
                            buttonIsEnabled = true,
                            textIsVisible = false,
                            progressIsVisible = false
                        )
                    LoadState.LOADING -> setLoadState(
                        buttonIsEnabled = false,
                        textIsVisible = false,
                        progressIsVisible = true
                    )
                    LoadState.SUCCESS -> {
                        setLoadState(
                            buttonIsEnabled = false,
                            textIsVisible = true,
                            progressIsVisible = false
                        )
                        findNavController().navigate(R.id.action_authFragment_to_profileFragment)
                    }
                    LoadState.ERROR -> {
                        setLoadState(
                            buttonIsEnabled = true,
                            textIsVisible = true,
                            progressIsVisible = false
                        )
                        binding.text.text = loadState.message
                    }
                }
            }
        }
    }

    private fun setLoadState(
        buttonIsEnabled: Boolean,
        textIsVisible: Boolean,
        progressIsVisible: Boolean
    ) {
        binding.btnAuth.isEnabled = buttonIsEnabled
        binding.text.isVisible = textIsVisible
        binding.progressBar.isVisible = progressIsVisible
    }

}




