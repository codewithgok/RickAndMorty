package com.meksconway.rickandmorty.data.datasource

import com.meksconway.rickandmorty.CharacterDetailQuery
import com.meksconway.rickandmorty.CharactersQuery
import com.meksconway.rickandmorty.EpisodeDetailQuery
import com.meksconway.rickandmorty.LocationDetailQuery
import com.meksconway.rickandmorty.common.Resource
import com.meksconway.rickandmorty.type.CustomType

interface RemoteDataSource {

    suspend fun getCharacters(page: Int): Resource<CharactersQuery.Data>

    suspend fun getCharacterDetail(id: String): Resource<CharacterDetailQuery.Data>

    suspend fun getLocationDetail(id: String): Resource<LocationDetailQuery.Data>

    suspend fun getEpisodesDetail(id: String): Resource<EpisodeDetailQuery.Data>

}