package com.meksconway.rickandmorty.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.meksconway.rickandmorty.base.BaseViewModel
import com.meksconway.rickandmorty.data.datasource.LocationsDataSource
import com.meksconway.rickandmorty.data.service.ApiService

class LocationVM
@ViewModelInject constructor(
    private val apiService: ApiService
) : BaseViewModel() {

    val locationsData = Pager(
        PagingConfig(pageSize = 3)
    ) {
        LocationsDataSource(apiService)
    }.flow.cachedIn(viewModelScope)


}