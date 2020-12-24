package com.meksconway.rickandmorty.ui.fragment

import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.meksconway.rickandmorty.R
import com.meksconway.rickandmorty.base.BaseFragment
import com.meksconway.rickandmorty.common.PageNavStatus
import com.meksconway.rickandmorty.common.RMNavigator
import com.meksconway.rickandmorty.common.Status.*
import com.meksconway.rickandmorty.common.viewBinding
import com.meksconway.rickandmorty.databinding.FragmentCharacterDetailBinding
import com.meksconway.rickandmorty.ui.adapter.characterdetail.CharacterDetailAdapter
import com.meksconway.rickandmorty.viewmodel.CharacterDetailVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharacterDetailFragment :
    BaseFragment<CharacterDetailVM>(R.layout.fragment_character_detail) {

    override val viewModel: CharacterDetailVM by viewModels()
    override val binding by viewBinding(FragmentCharacterDetailBinding::bind)
    override val pageStatus: PageNavStatus
        get() = PageNavStatus.childFragmentStatus(null)

    private val characterDetailAdapter = CharacterDetailAdapter()

    companion object {

        const val C_ID = "character_id"
        const val C_NAME = "character_name"

        fun newInstance(characterId: String?, characterName: String?): CharacterDetailFragment {
            return CharacterDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(C_ID, characterId)
                    putString(C_NAME, characterName)
                }
            }
        }
    }

    override fun viewDidLoad(savedInstanceState: Bundle?) {
        super.viewDidLoad(savedInstanceState)
        lifecycleScope.launchWhenResumed {
            val id = arguments?.getString(C_ID)
                ?: throw NullPointerException("character id must not be null")
            viewModel.getSingleCharacter(id)
            setPageStatus(PageNavStatus.childFragmentStatus(arguments?.getString(C_NAME)))

            binding.rvCharacterDetail.layoutManager = LinearLayoutManager(context)
            binding.rvCharacterDetail.adapter = characterDetailAdapter
        }

    }

    override fun observeViewModel(viewModel: CharacterDetailVM) {
        super.observeViewModel(viewModel)
        lifecycleScope.launch {
            viewModel.characterData.observe(viewLifecycleOwner) {
                when (it.status) {
                    SUCCESS -> {
                        characterDetailAdapter.submitList(it.data)
                        binding.progressBar.isVisible = false
                    }
                    FAIL -> {
                        showErrorLayout(it.errorMessage)
                        binding.progressBar.isVisible = false
                    }
                    LOADING -> binding.progressBar.isVisible = true
                }

            }
        }
    }

    private fun showErrorLayout(errorMessage: String?) {
        lifecycleScope.launchWhenResumed {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            delay(500)
            RMNavigator.popBack()
        }

    }

}
