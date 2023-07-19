package com.example.myapplication.ui.main.Digest

import androidx.lifecycle.ViewModelProvider
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
import com.example.myapplication.databinding.FragmentDetailBinding
import com.example.myapplication.databinding.FragmentDigestBinding
import com.example.myapplication.ui.main.Digest.adapter.DigestPagingAdapter
import com.example.myapplication.ui.main.detail.DetailViewModel
import com.example.myapplication.ui.main.model.Digest
import com.example.myapplication.ui.main.state.ClickableView
import kotlinx.coroutines.launch

class DigestFragment : Fragment() {

    private var _binding: FragmentDigestBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DigestViewModel by viewModels()
    private val adapter by lazy { DigestPagingAdapter { item -> onClick(item) } }


    private fun onClick(item: Digest) {
        val bundle = Bundle().apply {
            val id = item.id

            putString("id", id)
        }
   findNavController().navigate(R.id.action_digestFragment_to_digestDetailFragment, bundle)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDigestBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        observe()
        settingAdapter()
        initRefresher()
        loadStateItemsObserve()
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getDigest().collect {
                adapter.submitData(it)
            }
        }
    }

    private fun settingAdapter() {
        binding.digestRecycler.adapter = adapter
        binding.digestRecycler.itemAnimator?.changeDuration = 0
    }

    private fun initRefresher() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.digestRecycler.isVisible = true
            adapter.refresh()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun loadStateItemsObserve() {
        adapter.addLoadStateListener { loadState ->
            binding.error.isVisible =
                loadState.refresh is androidx.paging.LoadState.Error
            binding.recyclerProgressBar.isVisible =
                loadState.refresh is androidx.paging.LoadState.Loading
        }
    }

}