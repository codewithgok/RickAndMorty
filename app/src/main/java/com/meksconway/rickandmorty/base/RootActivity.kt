package com.meksconway.rickandmorty.base

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.meksconway.rickandmorty.R
import com.meksconway.rickandmorty.common.BottomTabs.*
import com.meksconway.rickandmorty.common.RMNavigator
import com.meksconway.rickandmorty.common.viewBinding
import com.meksconway.rickandmorty.databinding.ActivityRootBinding
import com.meksconway.rickandmorty.ui.fragment.CharactersFragment
import com.meksconway.rickandmorty.ui.fragment.EpisodeFragment
import com.meksconway.rickandmorty.ui.fragment.LocationFragment
import com.meksconway.rickandmorty.ui.view.RMNavBar
import com.meksconway.rickandmorty.viewmodel.RootVM
import com.meksconway.rickandmorty.viewmodel.SearchEventModel
import com.meksconway.rickandmorty.viewmodel.SearchType
import com.meksconway.rickandmorty.viewmodel.SearchVM
import com.trendyol.medusalib.navigator.MultipleStackNavigator
import com.trendyol.medusalib.navigator.Navigator
import com.trendyol.medusalib.navigator.NavigatorConfiguration
import com.trendyol.medusalib.navigator.transaction.NavigatorTransaction
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RootActivity : AppCompatActivity(), Navigator.NavigatorListener,
    RMNavBar.SearchBarFocusListener {

    private val viewModel by viewModels<RootVM>()
    private val binding by viewBinding(ActivityRootBinding::inflate)
    private val searchVM by viewModels<SearchVM>()

    private val rootFragmentProvider: List<() -> Fragment> =
        listOf(
            { CharactersFragment() },
            { LocationFragment() },
            { EpisodeFragment() }
        )

    private val navigator = MultipleStackNavigator(
        supportFragmentManager,
        R.id.container,
        rootFragmentProvider,
        this,
        NavigatorConfiguration(
            Characters.ordinal, true, NavigatorTransaction.ATTACH_DETACH
        )
    )

    override fun onDestroy() {
        RMNavigator.unregister()
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        navigator.initialize(savedInstanceState)
        RMNavigator.register(navigator)
        binding.rmNavBar.initFocusListener(this)
        observeViewModel()

        binding.bottomNav.onItemSelected = { navigator.switchTab(it) }

        binding.bottomNav.onItemReselected = {
            RMNavigator.backToRoot(it) { needScrollToTop ->
                if (needScrollToTop) {
                    viewModel.scrollToTop()
                }
            }
        }

        binding.rmNavBar.setSearchListener(
            RMNavBar.DebouncingQueryTextListener(lifecycle) {
                when (binding.bottomNav.itemActiveIndex) {
                    Characters.ordinal -> {
                        searchVM.setSearchText(SearchEventModel(it, SearchType.CHARACTER))
                    }
                    Locations.ordinal -> {
                        searchVM.setSearchText(SearchEventModel(it, SearchType.LOCATION))
                    }
                    Episodes.ordinal -> {
                        searchVM.setSearchText(SearchEventModel(it, SearchType.EPISODE))
                    }
                }
            }
        )

    }

    override fun onSaveInstanceState(outState: Bundle) {
        navigator.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }


    override fun onBackPressed() {
        RMNavigator.popBack { super.onBackPressed() }
    }

    private fun observeViewModel() {
        viewModel.getPageStatus().observe(this) {
            binding.fab.isVisible = it.isFilterBtnVisible
            binding.rmNavBar.setConfig(it.title, it.isBackVisible, it.isFilterBtnVisible)
        }
    }

    override fun onTabChanged(tabIndex: Int) {
        binding.bottomNav.itemActiveIndex = tabIndex
    }


    //RMNavBar.SearchBarFocusListener
    override fun onPrepare() {
        binding.bottomNav.isGone = true
    }

    override fun onFocused(view: View) {

    }

    override fun onClearFocus() {
        binding.bottomNav.isGone = false
    }
}