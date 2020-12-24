package com.meksconway.rickandmorty.base

import androidx.lifecycle.ViewModel
import com.meksconway.rickandmorty.viewmodel.SingleLiveEvent

open class BaseViewModel: ViewModel() {

    val needScrollTop = SingleLiveEvent<Boolean>()
    fun setScrollToTop() {
        needScrollTop.value = true
    }

}