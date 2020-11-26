package com.meksconway.rickandmorty.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.meksconway.rickandmorty.base.BaseViewModel
import com.meksconway.rickandmorty.data.datasource.CharactersDataSource
import com.meksconway.rickandmorty.data.service.ApiService

class CharactersVM @ViewModelInject constructor(
    private val apiService: ApiService
) : BaseViewModel() {

    var listData = Pager(PagingConfig(enablePlaceholders = true, pageSize = 10)) {
        CharactersDataSource(apiService)
    }.flow.cachedIn(viewModelScope)


}