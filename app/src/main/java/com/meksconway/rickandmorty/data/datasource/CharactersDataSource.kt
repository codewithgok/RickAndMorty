package com.meksconway.rickandmorty.data.datasource

import androidx.paging.PagingSource
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.exception.ApolloException
import com.meksconway.rickandmorty.CharactersQuery
import com.meksconway.rickandmorty.data.service.ApiService
import com.meksconway.rickandmorty.type.FilterCharacter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class CharactersDataSource constructor(
    private val apiService: ApiService,
    private val inputFilter: FilterCharacter? = null
) : PagingSource<Int, CharactersQuery.Result>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharactersQuery.Result> {
        try {
            val currentLoadingPageKey = params.key ?: 1
            val response =
                apiService.request(
                    CharactersQuery(
                        Input.optional(currentLoadingPageKey),
                        Input.optional(inputFilter)
                    )
                )
            val keyData = response.data?.characters
            if (response.hasErrors() || keyData == null) {
                return LoadResult.Error(Exception(response.errors?.first()?.message))
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