package com.meksconway.rickandmorty.data.service

import com.apollographql.apollo.api.Mutation
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.api.Response

interface ApiService {

    companion object {
        const val BASE_URL = "https://rickandmortyapi.com/graphql"
    }

    suspend fun <D : Operation.Data, T, V : Operation.Variables>
            request(mutation: Mutation<D, T, V>): Response<T>

    suspend fun <D : Operation.Data, T, V : Operation.Variables>
            request(query: Query<D, T, V>): Response<T>

}