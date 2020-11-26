package com.meksconway.rickandmorty.ui.fragment

import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.meksconway.rickandmorty.R
import com.meksconway.rickandmorty.base.BaseFragment
import com.meksconway.rickandmorty.common.PageNavStatus
import com.meksconway.rickandmorty.common.RMNavigator
import com.meksconway.rickandmorty.common.viewBinding
import com.meksconway.rickandmorty.databinding.FragmentLocationBinding
import com.meksconway.rickandmorty.ui.adapter.LoadMoreStateAdapter
import com.meksconway.rickandmorty.ui.adapter.LocationsAdapter
import com.meksconway.rickandmorty.viewmodel.LocationVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationFragment : BaseFragment<LocationVM>(R.layout.fragment_location) {

    override val viewModel: LocationVM by viewModels()
    override val binding by viewBinding(FragmentLocationBinding::bind)
    override val pageStatus = PageNavStatus.rootFragmentStatus(
        "Locations"
    )

    private val locationAdapter = LocationsAdapter { loc ->
        RMNavigator.navigateToLocationDetailFragment(
            loc?.id, loc?.name
        )
    }

    override fun viewDidLoad(savedInstanceState: Bundle?) {
        super.viewDidLoad(savedInstanceState)
        binding.rvLocations.layoutManager = LinearLayoutManager(context)
        binding.rvLocations.adapter = locationAdapter.withLoadStateFooter(
            footer = LoadMoreStateAdapter {
                locationAdapter.retry()
            }
        )

        lifecycleScope.launchWhenResumed {
            locationAdapter.addLoadStateListener { loadState ->
                if (loadState.refresh is LoadState.Loading) {
                    lifecycleScope.launchWhenResumed {
                        binding.progressBar.isVisible = true
                    }

                } else {
                    lifecycleScope.launchWhenResumed {
                        binding.progressBar.isGone = true
                    }

                    val error = when {
                        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                        else -> null
                    }
                    error?.let {
                        Toast.makeText(context, it.error.message, Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }

    }

    override fun observeViewModel(viewModel: LocationVM) {
        super.observeViewModel(viewModel)
        viewModel.needScrollTop.observe(viewLifecycleOwner) {
            if (it) binding.rvLocations.smoothScrollToPosition(0)
        }
        lifecycleScope.launch {
            viewModel.locationsData.collectLatest {
                locationAdapter.submitData(it)
            }
        }

    }


}