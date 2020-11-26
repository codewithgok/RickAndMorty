package com.meksconway.rickandmorty.data.datasource

import androidx.paging.PagingSource
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.exception.ApolloException
import com.meksconway.rickandmorty.LocationsQuery
import com.meksconway.rickandmorty.data.service.ApiService
import com.meksconway.rickandmorty.type.FilterLocation

class LocationsDataSource constructor(
    private val apiService: ApiService,
    private val inputFilter: FilterLocation? = null
) : PagingSource<Int, LocationsQuery.Result>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LocationsQuery.Result> {

        try {
            val currentLoadingPageKey = params.key ?: 1
            val response = apiService.request(
                LocationsQuery(
                    Input.optional(currentLoadingPageKey),
                    Input.optional(inputFilter)
                )
            )

            val keyData = response.data?.locations
            if (response.hasErrors() || keyData == null) {
                return LoadResult.Error(Exception(response.errors?.first()?.message))
            }
            return LoadResult.Page(
                data = keyData.results?.mapNotNull { it } ?: emptyList(),
                prevKey = keyData.info?.prev,
                nextKey = keyData.info?.next
            )

        }catch (e: ApolloException){
            return LoadResult.Error(e)
        }



    }
}