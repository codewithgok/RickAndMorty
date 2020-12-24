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
import com.meksconway.rickandmorty.common.viewBinding
import com.meksconway.rickandmorty.databinding.FragmentEpisodeBinding
import com.meksconway.rickandmorty.ui.adapter.EpisodesAdapter
import com.meksconway.rickandmorty.ui.adapter.LoadMoreStateAdapter
import com.meksconway.rickandmorty.viewmodel.EpisodeVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EpisodeFragment : BaseFragment<EpisodeVM>(R.layout.fragment_episode) {

    override val viewModel: EpisodeVM by viewModels()
    override val binding by viewBinding(FragmentEpisodeBinding::bind)
    override val pageStatus = PageNavStatus.rootFragmentStatus(
        "Episodes"
    )

    private val episodeAdapter = EpisodesAdapter()
    private var isFilterMode = false

    override fun viewDidLoad(savedInstanceState: Bundle?) {
        super.viewDidLoad(savedInstanceState)
        lifecycleScope.launchWhenResumed {

            binding.rvEpisodes.adapter = episodeAdapter.withLoadStateFooter(
                footer = LoadMoreStateAdapter {
                    episodeAdapter.retry()
                }
            )
            binding.rvEpisodes.layoutManager = LinearLayoutManager(context)

            episodeAdapter.addLoadStateListener { loadState ->
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

    override fun observeViewModel(viewModel: EpisodeVM) {
        super.observeViewModel(viewModel)
        viewModel.needScrollTop.observe(viewLifecycleOwner) {
            if (it) binding.rvEpisodes.smoothScrollToPosition(0)
        }
        lifecycleScope.launch {
            viewModel.episodeList.collectLatest {
                episodeAdapter.submitData(it)
            }
        }
    }


}