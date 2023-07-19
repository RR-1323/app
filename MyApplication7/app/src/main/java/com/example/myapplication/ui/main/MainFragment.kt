package com.example.myapplication.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMainBinding
import com.example.myapplication.ui.main.adapter.PhotoPagingAdapter
import com.example.myapplication.ui.main.state.ClickableView
import com.example.myapplication.ui.main.state.LoadState

import kotlinx.coroutines.launch


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory(
            activity?.application!!
        )
    }
    private val adapter by lazy {
        PhotoPagingAdapter { buttonState, item ->
            onClick(buttonState, item)
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    //    observe()
        loadStateItemsObserve()
        loadStateLike()
        settingAdapter()
        setSearchView()
        initRefresher()

    }
/*    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getPhoto().collect { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }*/

    private fun setSearchView() {
        val searchView = binding.searchBar.menu.getItem(0).actionView as SearchView
        searchView.setChangeTextListener { query ->
            viewModel.setQuery(query) { adapter.refresh() }
        }
    }


    private fun onClick(buttonState: ClickableView, item: Photo) {
        val bundle = Bundle().apply {
            val id = item.id

            putString("id", id)


        }

     //   findNavController().navigate(R.id.action_mainFragment_to_detailFragment, bundle)
        when (buttonState) {
            ClickableView.PHOTO -> findNavController().navigate(R.id.action_mainFragment_to_detailFragment, bundle)
            ClickableView.LIKE -> viewModel.like(item)
        }

    }


    fun SearchView.setChangeTextListener(block: (query: String) -> Unit) {

        this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                block(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        })
    }

    private fun settingAdapter() {
        binding.photoRecycler.adapter = adapter
        binding.photoRecycler.itemAnimator?.changeDuration = 0
    }

    private fun loadStateItemsObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->
                binding.error.isVisible =
                    loadState.mediator?.refresh is androidx.paging.LoadState.Error
                binding.recyclerProgressBar.isVisible =
                    loadState.mediator?.refresh is androidx.paging.LoadState.Loading
            }
        }
    }

    private fun loadStateLike() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadState.collect { loadStateLike ->
                binding.error.isVisible = loadStateLike == LoadState.ERROR
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