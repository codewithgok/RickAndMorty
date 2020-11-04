package com.meksconway.rickandmorty

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Mutation
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object NetworkManager {

    private var client: ApolloClient

    private const val BASE_URL = "https://rickandmortyapi.com/graphql"

    init {

        client = ApolloClient.builder()
            .serverUrl(BASE_URL)
            .okHttpClient(
                OkHttpClient.Builder()
                    .addInterceptor(
                        HttpLoggingInterceptor().apply {
                            setLevel(HttpLoggingInterceptor.Level.BODY)
                        }
                    )
                    .build()
            )
            .build()

    }

    fun <D : Operation.Data, T, V : Operation.Variables> request(query: Query<D, T, V>, callback: (Resource<T>) -> Unit) {
        callback.invoke(Resource.loading())
        client.query(query)
            .enqueue(object : ApolloCall.Callback<T>() {
                override fun onResponse(response: Response<T>) {
                    if (response.hasErrors() || response.data == null) {
                        callback.invoke(Resource.fail(response.errors?.first()?.message))
                    } else {
                        callback.invoke(Resource.success(response.data))
                    }
                }

                override fun onFailure(e: ApolloException) {
                    callback.invoke(Resource.fail(e.message))
                }

            })

    }

    fun <D : Operation.Data, T, V : Operation.Variables> request(mutation: Mutation<D, T, V>, callback: (Resource<T>) -> Unit) {
        callback.invoke(Resource.loading())
        client.mutate(mutation)
            .enqueue(object : ApolloCall.Callback<T>() {
                override fun onResponse(response: Response<T>) {
                    if (response.hasErrors() || response.data == null) {
                        callback.invoke(Resource.fail(response.errors?.first()?.message))
                    } else {
                        callback.invoke(Resource.success(response.data))
                    }
                }

                override fun onFailure(e: ApolloException) {
                    callback.invoke(Resource.fail(e.message))
                }

            })

    }

}