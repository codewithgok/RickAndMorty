package com.meksconway.rickandmorty.ui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.meksconway.rickandmorty.R
import com.meksconway.rickandmorty.base.BaseFragment
import com.meksconway.rickandmorty.common.PageNavStatus
import com.meksconway.rickandmorty.common.RMNavigator
import com.meksconway.rickandmorty.common.viewBinding
import com.meksconway.rickandmorty.databinding.FragmentCharactersBinding
import com.meksconway.rickandmorty.ui.adapter.CharactersAdapter
import com.meksconway.rickandmorty.ui.adapter.LoadMoreStateAdapter
import com.meksconway.rickandmorty.viewmodel.CharactersVM
import com.meksconway.rickandmorty.viewmodel.SearchType
import com.meksconway.rickandmorty.viewmodel.SearchVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharactersFragment : BaseFragment<CharactersVM>(R.layout.fragment_characters) {

    override val viewModel: CharactersVM by viewModels()
    override val binding by viewBinding(FragmentCharactersBinding::bind)
    override val pageStatus = PageNavStatus.rootFragmentStatus(
        "Characters"
    )
    private val searchVM by activityViewModels<SearchVM>()

    private val chAdapter = CharactersAdapter {
        it?.id ?: return@CharactersAdapter
        it.name ?: return@CharactersAdapter
        RMNavigator.navigateToCharacterDetailFragment(
            it.id, it.name
        )
    }
    private var isFilterMode = false


    override fun viewDidLoad(savedInstanceState: Bundle?) {
        super.viewDidLoad(savedInstanceState)
        setupRecyclerViewAdapter()
        setupRefreshLayout()
        setupAdapterLoadStateListener()
    }

    private fun setupAdapterLoadStateListener() {
        chAdapter.addLoadStateListener { loadState ->

            if (loadState.refresh is LoadState.Loading) {
                lifecycleScope.launchWhenResumed {
                    if (!binding.refreshLayout.isRefreshing) {
                        binding.progressBar.isVisible = !isFilterMode
                    }
                }

            } else {
                lifecycleScope.launchWhenResumed {
                    binding.progressBar.isVisible = false
                    binding.refreshLayout.isRefreshing = false
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

    private fun setupRefreshLayout() {
        binding.refreshLayout.setOnRefreshListener {
            chAdapter.refresh()
        }
    }

    private lateinit var layoutManager: GridLayoutManager
    private var spanCount = 2

    private fun setupRecyclerViewAdapter() {
        lifecycleScope.launchWhenResumed {

            spanCount = when (resources.configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    2
                }
                Configuration.ORIENTATION_LANDSCAPE -> {
                    4
                }
                else -> {
                    2
                }
            }

            layoutManager = GridLayoutManager(
                context,
                spanCount,
                LinearLayoutManager.VERTICAL,
                false
            )
            binding.rvCharacters.adapter = chAdapter.withLoadStateFooter(
                footer = LoadMoreStateAdapter {
                    chAdapter.retry()
                }
            )
            binding.rvCharacters.layoutManager = layoutManager


        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val orientation = newConfig.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager.spanCount = 2
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager.spanCount = 4
        }
    }

    override fun observeViewModel(viewModel: CharactersVM) {
        super.observeViewModel(viewModel)
        viewModel.needScrollTop.observe(viewLifecycleOwner) {
            binding.rvCharacters.smoothScrollToPosition(0)
        }
        lifecycleScope.launch {
            viewModel.listData.collectLatest {
                chAdapter.submitData(it)
            }

        }

        searchVM.searchEvent.observe(viewLifecycleOwner) {
            if (it.type == SearchType.CHARACTER) {
                this.isFilterMode = it.searchText != null
                it.searchText?.let { st ->
                    viewModel.setFilterSearch(st)
                    chAdapter.refresh()
                }
            }
        }

    }


}