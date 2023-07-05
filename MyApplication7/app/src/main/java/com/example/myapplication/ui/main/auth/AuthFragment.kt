package com.example.myapplication.ui.main.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
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
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authSuccessEventChannel.collect {
                    startActivity(Intent(requireContext(), MainActivity::class.java))
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
                }
            }
        }
        binding.btnAuth.setOnClickListener {
            
            openAuthPage()
        }
        
    }
    
    private fun openAuthPage() {
        viewModel.prepareAuthPageIntent { authorizeLauncher.launch(it) }
    }
    
    
//    fun getToken(authCode: String) {
//        lifecycleScope.launch(Dispatchers.IO) {
//            //  _loadState.emit(LoadState.START)
//            Log.d("onIntent", "getToken ${authCode}")
//            kotlin.runCatching {
//                retrofitT.getToken(code = authCode)
//
//            }.fold(
//                onSuccess = {
//                    accessToken = it.access_token
//                    Log.d("onIntent", "success token" + it.access_token)
//                    //   _loadState.emit(LoadState.SUCCESS)
//                },
//                onFailure = {
//                    Log.d("onIntent", "error token" + it.message ?: "")
//                    //   _loadState.emit(LoadState.ERROR)
//                }
//            )
//        }
//
//    }
//
//
//    fun handleDeepLink(intent: Intent) {
//        if (intent.action != Intent.ACTION_VIEW) return
//        val deepLinkUri = intent.data ?: return
//        if (deepLinkUri.queryParameterNames.contains("code")) {
//            authcode = deepLinkUri.getQueryParameter("code") ?: return
//            Log.d("deeplink", "authcode $authcode")
//            //  viewModel.getToken(authcode)
//            getToken(authcode)
//        }
//    }
//
//    fun composeUri(): Uri =
//        Uri.parse("https://unsplash.com/oauth/authorize")
//            .buildUpon()
//            .appendQueryParameter("client_id", ACCESS_KEY)
//            .appendQueryParameter("redirect_uri", REDIRECT_URI)
//            .appendQueryParameter("response_type", "code")
//            .appendQueryParameter("scope", "public")
//            .build()
    
    
    /*     private fun loadingObserve() {
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
                         //   progressIsVisible = false
                                   progressIsVisible = true
                         )
                         LoadState.SUCCESS -> {
                             setLoadState(
                                 buttonIsEnabled = false,
                                 textIsVisible = true,
                                 progressIsVisible = false
                             )
                            findNavController().navigate(R.id.action_authFragment_to_mainFragment)
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
         }*/
}


