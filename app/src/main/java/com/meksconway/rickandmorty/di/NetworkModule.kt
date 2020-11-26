package com.meksconway.rickandmorty.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.cache.http.HttpCachePolicy
import com.meksconway.rickandmorty.data.service.ApiService
import com.meksconway.rickandmorty.data.service.ApiServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideIgOkHttp(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
        }.build()
    }

    @Provides
    @Singleton
    fun provideApolloClient(okHttpClient: OkHttpClient): ApolloClient {
        return ApolloClient.builder()
            .serverUrl(ApiService.BASE_URL)
            .okHttpClient(okHttpClient)
            .defaultHttpCachePolicy(HttpCachePolicy.CACHE_FIRST)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(client: ApolloClient): ApiService {
        return ApiServiceImpl(client)
    }


}