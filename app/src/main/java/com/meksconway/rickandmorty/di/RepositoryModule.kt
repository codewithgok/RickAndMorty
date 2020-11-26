package com.meksconway.rickandmorty.di

import com.meksconway.rickandmorty.data.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindCharacterRepository(repository: CharacterRepositoryImpl): CharacterRepository

    @Binds
    abstract fun bindLocationRepository(repository: LocationRepositoryImpl): LocationRepository

    @Binds
    abstract fun bindEpisodeRepository(repository: EpisodeRepositoryImpl): EpisodeRepository

}