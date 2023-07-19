package com.example.myapplication.ui.main.Digest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDigestDetailBinding
import com.example.myapplication.ui.main.Photo
import com.example.myapplication.ui.main.ViewModelFactory
import com.example.myapplication.ui.main.adapter.PhotoPagingAdapter
import com.example.myapplication.ui.main.adapter.loadImage
import com.example.myapplication.ui.main.state.ClickableView
import com.example.myapplication.ui.main.state.LoadState
import kotlinx.coroutines.launch

class DigestDetailFragment : Fragment() {

    private var _binding: FragmentDigestDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DigestDetailViewModel by viewModels {
        ViewModelFactory(
            activity?.application!!
        )
    }

    private val adapter by lazy {
        PhotoPagingAdapter { buttonState, item ->
            onClick(buttonState, item)
        }
    }

    private fun onClick(buttonState: ClickableView, item: Photo) {
        val bundle = Bundle().apply {
            val id = item.id

            putString("id", id)


        }


        findNavController().navigate(R.id.action_digestDetailFragment_to_detailFragment, bundle)


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDigestDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getString("id")
    //    observe(id.toString())
        loadStateObserver(id.toString())
        loadStateItemsObserve()
        loadStateLike()
        settingAdapter()
        initRefresher()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

  /*  private fun observe(id: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.setId(id) { adapter.refresh() }
            viewModel.getPhoto().collect { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }
*/
    private fun loadStateObserver(id: String) {
        viewModel.getDigestInfo(id)
        viewLifecycleOwner.lifecycleScope
            .launchWhenStarted {
                viewModel.loadState.collect { loadState -> updateUiOnServerResponse(loadState) }
            }
    }

    private fun updateUiOnServerResponse(loadState: LoadState) {
        if (loadState == LoadState.ERROR) {
            binding.error.isVisible = true
        }
        if (loadState == LoadState.SUCCESS) {
            viewLifecycleOwner.lifecycleScope
                .launchWhenStarted {
                    viewModel.state
                        .collect { state -> showInfo(state) }
                }
        }
    }

    private fun showInfo(state: DigestState) {
        when (state) {
            DigestState.NotStartedYet ->
                binding.toolProgressBar.visibility = View.VISIBLE
            is DigestState.Success -> {
                binding.toolProgressBar.visibility = View.GONE
                binding.collapsingToolbarLayout.title = state.data.title
                binding.digestTitle.text = state.data.title
                binding.description.text = state.data.description
                binding.tags.text = state.data.tags.joinToString { tag ->
                    "#${tag.title}"
                }
                /*  binding.data.text =
                      resources.getQuantityString(
                          R.plurals.digest_data,
                          state.data.totalPhotos,
                          state.data.totalPhotos,
                          state.data.userUsername
                      )*/
                binding.preview.loadImage(state.data.previewPhoto)
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

}