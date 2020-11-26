package com.meksconway.rickandmorty.data.repository

import com.meksconway.rickandmorty.common.Resource
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    suspend fun getSingleCharacter(id: String): Flow<Resource<List<CharacterDetailItem>>>

//https://github.com/rajandev17/Plasma/tree/master/core/src/main

}