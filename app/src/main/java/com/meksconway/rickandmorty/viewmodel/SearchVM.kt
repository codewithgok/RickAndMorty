package com.meksconway.rickandmorty.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

enum class SearchType {
    CHARACTER, LOCATION, EPISODE
}

data class SearchEventModel(
    val searchText: String?,
    val type: SearchType
)

class SearchVM : ViewModel() {

    private val _searchEvent = SingleLiveEvent<SearchEventModel>()
    val searchEvent: LiveData<SearchEventModel>
        get() = _searchEvent


    fun setSearchText(event: SearchEventModel) {
        _searchEvent.value = event
    }


}