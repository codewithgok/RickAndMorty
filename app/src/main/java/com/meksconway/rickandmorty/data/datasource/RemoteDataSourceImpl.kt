package com.meksconway.rickandmorty.data.datasource

import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.meksconway.rickandmorty.CharacterDetailQuery
import com.meksconway.rickandmorty.CharactersQuery
import com.meksconway.rickandmorty.EpisodeDetailQuery
import com.meksconway.rickandmorty.LocationDetailQuery
import com.meksconway.rickandmorty.common.Resource
import com.meksconway.rickandmorty.data.service.ApiService
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) :
    RemoteDataSource {

    override suspend fun getCharacters(page: Int): Resource<CharactersQuery.Data> {
        return getResult {
            apiService.request(CharactersQuery(page = Input.optional(page)))
        }
    }

    override suspend fun getCharacterDetail(id: String): Resource<CharacterDetailQuery.Data> {
        return getResult {
            apiService.request(
                CharacterDetailQuery(
                    id = id
                )
            )
        }
    }

    override suspend fun getLocationDetail(id: String): Resource<LocationDetailQuery.Data> {
        return getResult {
            apiService.request(
                LocationDetailQuery(id)
            )
        }
    }

    override suspend fun getEpisodesDetail(id: String): Resource<EpisodeDetailQuery.Data> {
        return getResult {
            apiService.request(
                EpisodeDetailQuery(id)
            )
        }
    }


    private suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {

        return try {
            val response = call()
            val data = response.data
            if (response.hasErrors() || data == null) {
                Resource.fail(response.errors?.get(0)?.message)
            } else {
                Resource.success(data)
            }

        } catch (e: ApolloException) {
            return Resource.fail(e.message)
        }

    }

}