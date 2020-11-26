package com.meksconway.rickandmorty.data.datasource

import androidx.paging.PagingSource
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.exception.ApolloException
import com.meksconway.rickandmorty.EpisodesQuery
import com.meksconway.rickandmorty.data.service.ApiService
import com.meksconway.rickandmorty.type.FilterEpisode

class EpisodesDataSource constructor(
    private val apiService: ApiService,
    private val inputFilter: FilterEpisode? = null
) : PagingSource<Int, EpisodesQuery.Result>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EpisodesQuery.Result> {

        try {
            val currentLoadingPageKey = params.key ?: 1
            val response = apiService.request(
                EpisodesQuery(
                    Input.optional(currentLoadingPageKey),
                    Input.optional(inputFilter)
                )
            )
            val keyData = response.data?.episodes
            if (response.hasErrors() || keyData == null) {
                return LoadResult.Error(Exception(response.errors?.get(0)?.message))
            }

            return LoadResult.Page(
                data = keyData.results?.mapNotNull { it } ?: emptyList(),
                prevKey = keyData.info?.prev,
                nextKey = keyData.info?.next
            )

        } catch (e: ApolloException) {
            return LoadResult.Error(e)
        }

    }
}