package com.example.myapplication.ui.main.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentProfileBinding

import com.example.myapplication.ui.main.adapter.PhotoPagingAdapter
import com.example.myapplication.ui.main.auth.TOKEN_ENABLED_KEY
import com.example.myapplication.ui.main.auth.TOKEN_SHARED_KEY
import com.example.myapplication.ui.main.Photo
import com.example.myapplication.ui.main.ViewModelFactory
import com.example.myapplication.ui.main.auth.TOKEN_SHARED_NAME
import com.example.myapplication.ui.main.state.ClickableView
import com.example.myapplication.ui.main.state.LoadState
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels {
        ViewModelFactory(activity?.application!!)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    private val adapter by lazy {
        PhotoPagingAdapter { buttonState, item ->
            onClick(buttonState, item)
        }
    }

    private var location: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       viewLifecycleOwner.lifecycleScope.launch {


           getLoadingState()
           loadStateItemsObserve()
           loadStateLike()

           initRefresher()
           setLocationClick()

           setUpLogoutButton(createSharedPreference(TOKEN_SHARED_NAME))

           settingAdapter()
           //observe()

        }


    }
    fun createSharedPreference(sharedName: String) =
        requireContext().getSharedPreferences(sharedName, Context.MODE_PRIVATE)


/*

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.getUserProfile()
                viewModel.getPhoto().collect { pagingData ->
                    adapter.submitData(pagingData)
                }
            }
        }
    }
*/
    private fun getLoadingState() {
        viewModel.getProfile()
        viewLifecycleOwner.lifecycleScope
            .launchWhenStarted {
                viewModel.loadState.collect { loadState -> updateUiOnServerResponse(loadState) }
            }
    }

    private fun updateUiOnServerResponse(loadState: LoadState) {
        if (loadState == LoadState.ERROR) {
            binding.error.isVisible = true
            binding.locationButton.isEnabled = false
        }
        if (loadState == LoadState.SUCCESS) {
            viewLifecycleOwner.lifecycleScope
                .launchWhenStarted {
                    viewModel.state
                        .collect { state ->  showInfo(state) }
                }
        }
    }

    @SuppressLint("StringFormatInvalid")
    private fun showInfo(state: ProfileState) {


        when (state) {
            ProfileState.NotStartedYet -> {}
            is ProfileState.Success -> {
                binding.locationButton.isEnabled = true
                state.data.name?.let { viewModel.setUsername(it) { adapter.refresh() } }
                binding.location.text = state.data.id
           /*     if (state.data.location == null) */binding.locationString.visibility = View.GONE
                binding.username.text = state.data.name.toString()
                binding.name.text = state.data.name
             //   binding.likes.text = getString(R.string.user_total_likes, state.data.totalLikes)
                binding.avatar.loadImage(state.data.iconImg.toString())
                location = state.data.goldExpiration
            }
        }


    }
    fun ImageView.loadImage(urls: String) {
        Glide.with(this)
            .load(urls)
            .error(R.drawable.error_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.placeholder)
            .into(this)
    }



    private fun onClick(buttonState: ClickableView, item: Photo) {
        when (buttonState) {
            ClickableView.PHOTO -> {

                val bundle = Bundle().apply {
                    val id = item.id

                    putString("id", id)



                }

                findNavController().navigate(R.id.action_profileFragment_to_detailFragment, bundle)
            }
            /*  findNavController().navigate(ProfileFragmentDirections
              .actionNavigationUserToNavigationPhotoDetails(item.id))*/
            ClickableView.LIKE -> {
           //     viewModel.like(item)
             //   getLoadingState()
            }
        }
    }

    private fun settingAdapter() {
        binding.photoRecycler.adapter = adapter
        binding.photoRecycler.itemAnimator?.changeDuration = 0
    }

    private fun loadStateItemsObserve() {
        adapter.addLoadStateListener { loadState ->
            binding.error.isVisible =
                loadState.mediator?.refresh is androidx.paging.LoadState.Error
            binding.recyclerProgressBar.isVisible =
                loadState.mediator?.refresh is androidx.paging.LoadState.Loading
        }
    }

    private fun loadStateLike() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadState.collect { loadStateLike ->
                binding.error.isVisible =
                    loadStateLike == LoadState.ERROR
            }
        }
    }

    private fun initRefresher() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.photoRecycler.isVisible = true
            adapter.refresh()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun showLocationOnMap(locationUri: Uri) {
        val mapIntent = Intent(Intent.ACTION_VIEW).apply { data = locationUri }
        startActivity(mapIntent)
    }

    private fun setLocationClick() {
        binding.locationButton.setOnClickListener {
            if (location != null) {
                showLocationOnMap(Uri.parse("geo:0,0?q=$location"))
            }
        }
    }

    private fun setUpLogoutButton(preferences: SharedPreferences) {
        val button = binding.logoutBar.menu.getItem(0)
        button?.setOnMenuItemClickListener {
            setUpAlertDialog(preferences)
            true
        }
    }

    private fun setUpAlertDialog(preferences: SharedPreferences) {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle(R.string.logout_title)
            .setMessage(R.string.logout_message)
            .setPositiveButton(R.string.yes) { _, _ ->
                preferences.edit().putString(TOKEN_SHARED_KEY, "").apply()
                preferences.edit().putBoolean(TOKEN_ENABLED_KEY, false).apply()

                findNavController().navigate(R.id.action_profileFragment_to_authFragment)
            }
            .setNegativeButton(R.string.no) { _, _ ->
                dialog.create().hide()
            }
        dialog.create().show()
    }
}
