package com.meksconway.rickandmorty.di

import com.meksconway.rickandmorty.data.datasource.RemoteDataSource
import com.meksconway.rickandmorty.data.datasource.RemoteDataSourceImpl
import com.meksconway.rickandmorty.data.service.ApiService
import com.meksconway.rickandmorty.data.service.ApiServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindRemoteDataSource(ds: RemoteDataSourceImpl): RemoteDataSource


}