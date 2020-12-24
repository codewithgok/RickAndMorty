package com.meksconway.rickandmorty.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.apollographql.apollo.api.Input
import com.meksconway.rickandmorty.base.BaseViewModel
import com.meksconway.rickandmorty.common.DataSourceFilterType
import com.meksconway.rickandmorty.common.DataSourceFilterType.*
import com.meksconway.rickandmorty.data.datasource.CharactersDataSource
import com.meksconway.rickandmorty.data.service.ApiService
import com.meksconway.rickandmorty.type.FilterCharacter
import javax.inject.Inject


class CharactersDataSourceFactory @Inject constructor(
    private val apiService: ApiService
) {

    fun create(
        pair: Pair<DataSourceFilterType, FilterCharacter?>
    ): CharactersDataSource {
        return when (pair.first) {
            CLEAR -> CharactersDataSource(apiService)
            FILTER -> CharactersDataSource(apiService)
            JUST_NAME -> CharactersDataSource(
                apiService, pair.second
            )
        }
    }

}

class CharactersVM @ViewModelInject constructor(
    private val factory: CharactersDataSourceFactory
) : BaseViewModel() {

    var filterKey: String? = null
    var listData = Pager(PagingConfig(enablePlaceholders = true, pageSize = 10)) {
        if (filterKey == null) {
            factory.create(Pair(CLEAR, null))
        } else {
            factory.create(
                Pair(
                    JUST_NAME, FilterCharacter(
                        name = Input.optional(filterKey)
                    )
                )
            )
        }

    }.flow.cachedIn(viewModelScope)

    fun clearFilter() {
        filterKey = null
    }

    fun setFilterSearch(searchKey: String?) {
        filterKey = searchKey
    }

    override fun onCleared() {
        filterKey = null
        super.onCleared()
    }


}