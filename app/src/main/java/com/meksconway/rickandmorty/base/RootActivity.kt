package com.meksconway.rickandmorty.base

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.meksconway.rickandmorty.R
import com.meksconway.rickandmorty.common.BottomTabs.Characters
import com.meksconway.rickandmorty.common.RMNavigator
import com.meksconway.rickandmorty.common.hideKeyboard
import com.meksconway.rickandmorty.common.showKeyboard
import com.meksconway.rickandmorty.common.viewBinding
import com.meksconway.rickandmorty.databinding.ActivityRootBinding
import com.meksconway.rickandmorty.ui.fragment.CharactersFragment
import com.meksconway.rickandmorty.ui.fragment.EpisodeFragment
import com.meksconway.rickandmorty.ui.fragment.LocationFragment
import com.meksconway.rickandmorty.viewmodel.RootVM
import com.trendyol.medusalib.navigator.MultipleStackNavigator
import com.trendyol.medusalib.navigator.Navigator
import com.trendyol.medusalib.navigator.NavigatorConfiguration
import com.trendyol.medusalib.navigator.transaction.NavigatorTransaction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

@AndroidEntryPoint
class RootActivity : AppCompatActivity(), Navigator.NavigatorListener {

    private val viewModel by viewModels<RootVM>()
    private val binding by viewBinding(ActivityRootBinding::inflate)

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
        observeViewModel()

        KeyboardVisibilityEvent.setEventListener(this, object : KeyboardVisibilityEventListener {
            override fun onVisibilityChanged(isOpen: Boolean) {
                binding.bottomNav.isGone = isOpen
            }
        })

        binding.btnBack.setOnClickListener {
            if (navigator.canGoBack()) {
                navigator.goBack()
            }
        }

        binding.btnSearch.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                binding.lytSearch.isVisible = true
                delay(300)
                showKeyboard(binding.edtSearch)
            }
        }

        binding.btnCloseSearch.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                binding.lytSearch.isVisible = false
                hideKeyboard(binding.root)
            }
        }

        binding.bottomNav.onItemSelected = { navigator.switchTab(it) }

        binding.bottomNav.onItemReselected = {
            RMNavigator.backToRoot(it) { needScrollToTop ->
                if (needScrollToTop) {
                    viewModel.scrollToTop()
                }
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        navigator.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }


    override fun onBackPressed() {

        if (binding.lytSearch.isVisible) {
            binding.lytSearch.isVisible = false
            return
        }

        if (navigator.canGoBack()) {
            navigator.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun observeViewModel() {
        viewModel.getPageStatus().observe(this) {
            binding.btnBack.isVisible = it.isBackVisible
            binding.btnSearch.isVisible = it.isFilterBtnVisible
            binding.txtTitle.text = it.title
        }
    }

    override fun onTabChanged(tabIndex: Int) {
        binding.bottomNav.itemActiveIndex = tabIndex
    }
}