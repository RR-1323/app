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
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMainBinding
import com.example.myapplication.ui.main.adapter.PhotoPagingAdapter
import com.example.myapplication.ui.main.state.ClickableView

import kotlinx.coroutines.launch


class MainFragment : Fragment() {



       //private val throwable = MutableLiveData<Throwable?>(null)
    private var _binding: FragmentMainBinding? = null

    // private  var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!


  //  val viewModel = ViewModelProvider(this, ViewModelFactory)[MainViewModel::class.java]
  private val viewModel: MainViewModel by viewModels()

    private val adapter by lazy {
        PhotoPagingAdapter { buttonState, item ->
            onClick(buttonState, item)
        }
    }
 //   private val pagedAdapter = PhotoPagingAdapter { item -> onItemClick(item) }


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
        observe()
        loadStateItemsObserve()
        loadStateLike()
        settingAdapter()

        initRefresher()
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.pagedMovies.collect { pagingData ->
                adapter.submitData(pagingData)
            }

        }
    }

    private fun onClick(buttonState: ClickableView, item: photolistnew.photoNewItem) {
        val bundle = Bundle().apply {
            val id = item.id

            putString("id", id)

//            putString("status", status)
//            putString("type", type)
//
//            putString("location", location)
//            putString("episode", episode)

        }

        findNavController().navigate(R.id.action_mainFragment_to_detailFragment, bundle)



//        when (buttonState) {
//            ClickableView.PHOTO -> findNavController().navigate(R.id.action_mainFragment_to_detailFragment)
//            ClickableView.LIKE -> viewModel.like(item)
//        }
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
            viewModel.isLoading.collect(){
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