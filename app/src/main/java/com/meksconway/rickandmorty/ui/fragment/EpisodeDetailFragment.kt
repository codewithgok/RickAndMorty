package com.meksconway.rickandmorty.ui.fragment

import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.meksconway.rickandmorty.R
import com.meksconway.rickandmorty.base.BaseFragment
import com.meksconway.rickandmorty.common.PageNavStatus
import com.meksconway.rickandmorty.common.Status.*
import com.meksconway.rickandmorty.common.viewBinding
import com.meksconway.rickandmorty.databinding.FragmentEpisodeDetailBinding
import com.meksconway.rickandmorty.ui.adapter.episodedetail.EpisodeDetailAdapter
import com.meksconway.rickandmorty.viewmodel.EpisodeDetailVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EpisodeDetailFragment : BaseFragment<EpisodeDetailVM>(R.layout.fragment_episode_detail) {

    override val viewModel: EpisodeDetailVM by viewModels()
    override val binding by viewBinding(FragmentEpisodeDetailBinding::bind)
    override val pageStatus = PageNavStatus.childFragmentStatus(null)


    private val episodeDetailAdapter = EpisodeDetailAdapter()

    override fun viewDidLoad(savedInstanceState: Bundle?) {
        super.viewDidLoad(savedInstanceState)
        lifecycleScope.launchWhenResumed {
            val id = arguments?.getString(E_ID)
                ?: throw NullPointerException("episode id must not be null")

            viewModel.getSingleEpisode(id)
            setPageStatus(
                PageNavStatus.childFragmentStatus(
                    arguments?.getString(
                        E_NAME
                    )
                )
            )

            binding.rvEpisodeDetail.layoutManager = LinearLayoutManager(context)
            binding.rvEpisodeDetail.adapter = episodeDetailAdapter
        }
    }


    override fun observeViewModel(viewModel: EpisodeDetailVM) {
        super.observeViewModel(viewModel)
        viewModel.characterData.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                when (it.status) {
                    SUCCESS -> {
                        episodeDetailAdapter.submitList(it.data)
                        binding.progressBar.isGone = true
                    }
                    FAIL -> {
                        binding.progressBar.isGone = true
                        Toast.makeText(context, it.errorMessage, Toast.LENGTH_SHORT).show()
                    }
                    LOADING -> {
                        binding.progressBar.isGone = false
                    }
                }
            }
        }
    }


    companion object {

        const val E_ID = "episode_id"
        const val E_NAME = "episode_name"

        fun newInstance(episodeId: String?, episodeName: String?): EpisodeDetailFragment {
            return EpisodeDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(E_ID, episodeId)
                    putString(E_NAME, episodeName)
                }
            }
        }
    }


}