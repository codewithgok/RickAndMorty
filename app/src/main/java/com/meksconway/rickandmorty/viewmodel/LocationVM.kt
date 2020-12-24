package com.meksconway.rickandmorty.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.apollographql.apollo.api.Input
import com.meksconway.rickandmorty.base.BaseViewModel
import com.meksconway.rickandmorty.common.DataSourceFilterType
import com.meksconway.rickandmorty.data.datasource.LocationsDataSource
import com.meksconway.rickandmorty.data.service.ApiService
import com.meksconway.rickandmorty.type.FilterLocation
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LocationsDataSourceFactory @Inject constructor(
    private val apiService: ApiService
) {

    fun create(pair: Pair<DataSourceFilterType, FilterLocation?>): LocationsDataSource {
        return when (pair.first) {
            DataSourceFilterType.CLEAR -> LocationsDataSource(apiService)
            DataSourceFilterType.FILTER -> LocationsDataSource(apiService)
            DataSourceFilterType.JUST_NAME -> LocationsDataSource(
                apiService, pair.second
            )
        }
    }

}

class LocationVM
@ViewModelInject constructor(
    private val factory: LocationsDataSourceFactory
) : BaseViewModel() {

    val locationsData = Pager(
        PagingConfig(pageSize = 3)
    ) {
        if (filterKey == null) {
            factory.create(Pair(DataSourceFilterType.CLEAR, null))
        } else {
            factory.create(
                Pair(
                    DataSourceFilterType.JUST_NAME,
                    FilterLocation(
                        name = Input.optional(filterKey)
                    )
                )

            )
        }
    }.flow.cachedIn(viewModelScope)

    private var filterKey: String? = null

    fun setFilter(searchKey: String?) {
        filterKey = searchKey
    }

    override fun onCleared() {
        filterKey = null
        super.onCleared()
    }


}