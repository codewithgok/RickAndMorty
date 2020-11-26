package com.meksconway.rickandmorty.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.meksconway.rickandmorty.base.BaseViewModel
import com.meksconway.rickandmorty.common.PageNavStatus

class RootVM: BaseViewModel() {

    private val pageStatus = MutableLiveData<PageNavStatus>()
    fun getPageStatus(): LiveData<PageNavStatus> = pageStatus
    fun setPageStatus(status: PageNavStatus) {
        this.pageStatus.value = status
    }

    val scrollTop = SingleLiveEvent<Boolean>()
    fun scrollToTop() {
        scrollTop.value = true
    }


}