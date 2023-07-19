package com.example.myapplication.ui.main.onboarding

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentOnboardingBinding
import com.example.myapplication.databinding.FragmentProfileBinding
import com.example.myapplication.ui.main.auth.ONBOARDING_IS_SHOWN
import com.example.myapplication.ui.main.auth.TOKEN_SHARED_NAME
import com.example.myapplication.ui.main.profile.ProfileViewModel
import com.google.android.material.tabs.TabLayoutMediator

class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OnboardingViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnboardingBinding.inflate(inflater)
        return binding.root
    }




    private var mediator: TabLayoutMediator? = null



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewPager()
        setTabs()
        setAuthorizeButton()
        saveOnbordingShown()
        if (saveOnbordingShown()){
         //   findNavController().navigate(R.id.action_onboardingFragment_to_authFragment)
        }
    }

    private fun setViewPager() {
        binding.viewPager.adapter =
            ViewPagerAdapter(resources.getStringArray(R.array.onboarding_texts_array))
       // binding.viewPager.registerOnPageChangeCallback(AnimateImageOnPageChange(binding.ellipseImage))
    }

    private fun setTabs() {
        mediator = TabLayoutMediator(binding.tabs, binding.viewPager) { _, _ -> }
        mediator!!.attach()
    }

    private fun setAuthorizeButton() {
        binding.authorizeButton.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_authFragment)
        }
    }

    private fun saveOnbordingShown(): Boolean {
        val prefs = createSharedPreference(TOKEN_SHARED_NAME)
        prefs.edit().putBoolean(ONBOARDING_IS_SHOWN, true).apply()
        return  prefs.getBoolean(ONBOARDING_IS_SHOWN, true)
    }
  fun createSharedPreference(sharedName: String) =
        requireContext().getSharedPreferences(sharedName, Context.MODE_PRIVATE)

    override fun onDestroyView() {
        super.onDestroyView()
        mediator?.detach()
        mediator = null
    }
}

class AnimateImageOnPageChange(val image: ImageView): ViewPager2.OnPageChangeCallback() {
    override fun onPageScrolled(
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
    ) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels)
        image.translationY = (positionOffset + position) * 100
    }
}