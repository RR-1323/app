package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.example.myapplication.databinding.FragmentDigestBinding
import com.example.myapplication.databinding.FragmentStartBinding
import com.example.myapplication.ui.main.auth.ONBOARDING_IS_SHOWN
import com.example.myapplication.ui.main.auth.TOKEN_ENABLED_KEY
import com.example.myapplication.ui.main.auth.TOKEN_SHARED_NAME

class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = createSharedPreference(TOKEN_SHARED_NAME)
        val toOnboardingFragment = StartFragmentDirections.actionStartFragmentToOnboardingFragment()
        val toAuthFragment = StartFragmentDirections.actionStartFragmentToAuthFragment()
        val toPhotosFragment = StartFragmentDirections.actionStartFragmentToMainFragment()


        if (prefs.getBoolean(ONBOARDING_IS_SHOWN, true)) {
            findNavController().navigate(toAuthFragment)

        } else {
            findNavController().navigate(toOnboardingFragment)

        }
    }

    fun createSharedPreference(sharedName: String) =
        requireContext().getSharedPreferences(sharedName, Context.MODE_PRIVATE)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}