package com.meksconway.rickandmorty.ui.view

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import com.meksconway.rickandmorty.common.RMNavigator
import com.meksconway.rickandmorty.common.hideKeyboard
import com.meksconway.rickandmorty.common.showKeyboard
import com.meksconway.rickandmorty.databinding.ViewRmnavbarBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RMNavBar(context: Context, attrs: AttributeSet) : CardView(context, attrs),
    RMNavigator.BackHandler {

    private val binding = ViewRmnavbarBinding.inflate(LayoutInflater.from(context), this, true)
    private var searchDelegate: SearchListener? = null
    private var searchFocusDelegate: SearchBarFocusListener? = null


    fun initFocusListener(listener: SearchBarFocusListener) {
        this.searchFocusDelegate = listener
    }

    fun setSearchListener(delegate: SearchListener) {
        this.searchDelegate = delegate
    }

    init {

        RMNavigator.register(this)
        binding.edtSearch.doAfterTextChanged {
            it?.toString()?.let { text ->
                searchDelegate?.onSearch(text)
            }
        }

        binding.btnBack.setOnClickListener {
            RMNavigator.popBack()
        }

        binding.btnCloseSearch.setOnClickListener {
            if (binding.lytSearch.isVisible) {
                RMNavigator.popBack()
            }
        }

        binding.btnSearch.setOnClickListener {
            if (binding.lytSearch.isGone) {
                binding.lytSearch.isVisible = true
                searchFocusDelegate?.onPrepare()
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        context.showKeyboard(binding.btnSearch)
                        binding.btnSearch.requestFocus()
                        searchFocusDelegate?.onFocused(binding.btnSearch)
                    }, 300
                )
            }
        }
    }


    fun setConfig(
        title: String?,
        isBackButtonVisible: Boolean = false,
        isSearchButtonVisible: Boolean = false
    ) {
        binding.txtTitle.text = title
        binding.btnBack.isVisible = isBackButtonVisible
        binding.btnSearch.isVisible = isSearchButtonVisible
    }

    internal class DebouncingQueryTextListener(
        lifecycle: Lifecycle,
        private val onDebouncingQueryTextChange: (String?) -> Unit
    ) : SearchListener {
        private var debouncePeriod: Long = 300

        private val coroutineScope = lifecycle.coroutineScope

        private var searchJob: Job? = null

        override fun onSearch(searchText: String) {
            searchJob?.cancel()
            searchJob = coroutineScope.launch {
                delay(debouncePeriod)
                onDebouncingQueryTextChange(searchText)
            }
        }

    }


    interface SearchBarFocusListener {
        fun onPrepare()
        fun onFocused(view: View)
        fun onClearFocus()
    }

    interface SearchListener {
        fun onSearch(searchText: String)
    }

    override val canBack: Boolean
        get() = binding.lytSearch.isGone

    override fun onBackPressed() {
        binding.edtSearch.text = null
        binding.edtSearch.clearFocus()
        context.hideKeyboard(binding.edtSearch)
        binding.lytSearch.isVisible = false
        Handler(Looper.getMainLooper()).postDelayed({
            searchFocusDelegate?.onClearFocus()
        }, 300)

    }

}