package com.meksconway.rickandmorty.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import com.meksconway.rickandmorty.databinding.ViewRmnavbarBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RMNavBar(context: Context, attrs: AttributeSet) : CardView(context, attrs) {

    private val binding = ViewRmnavbarBinding.inflate(LayoutInflater.from(context), this, true)
    private var searchDelegate: SearchListener? = null
    private var navBarDelegate: NavBarListener? = null



    init {
        binding.edtSearch.doAfterTextChanged {
            it?.toString()?.let { text ->
                searchDelegate?.onSearch(text)
            }
        }
        binding.btnBack.setOnClickListener {
            navBarDelegate?.backPressed()
        }
    }

    internal class DebouncingQueryTextListener(
        lifecycle: Lifecycle,
        private val onDebouncingQueryTextChange: (String?) -> Unit
    ) : SearchListener {
        var debouncePeriod: Long = 300

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


    interface NavBarListener {
        fun backPressed()
    }

    interface SearchListener {
        fun onSearch(searchText: String)
    }

}