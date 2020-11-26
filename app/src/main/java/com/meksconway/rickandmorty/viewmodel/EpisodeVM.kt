package com.meksconway.rickandmorty.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.meksconway.rickandmorty.EpisodesQuery
import com.meksconway.rickandmorty.base.BaseViewModel
import com.meksconway.rickandmorty.common.getSeasonName
import com.meksconway.rickandmorty.data.datasource.EpisodesDataSource
import com.meksconway.rickandmorty.data.service.ApiService
import com.meksconway.rickandmorty.ui.adapter.EpisodeItemType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EpisodeVM
@ViewModelInject constructor(
    private val apiService: ApiService
) : BaseViewModel() {

    val episodeList: Flow<PagingData<EpisodeItemType>> =
        getEpisodes().map {
            pagingData -> pagingData.map { EpisodeItemType.Episode(it) }
        }.map {
            it.insertSeparators { before, after ->
                if (after == null) {
                    // end of the list
                    return@insertSeparators null
                }

                if (before == null) {
                    // start of the list
                    return@insertSeparators EpisodeItemType.Header(
                        seasonName = getSeasonName(after.episodes.episode)
                    )
                }

                if (before.episodes.episode?.take(3) != after.episodes.episode?.take(3)){
                    return@insertSeparators EpisodeItemType.Header(
                        seasonName = getSeasonName(after.episodes.episode)
                    )
                } else {
                    return@insertSeparators null
                }
            }
        }.cachedIn(viewModelScope)


    private fun getEpisodes(): Flow<PagingData<EpisodesQuery.Result>> = Pager(
        PagingConfig(pageSize = 3)
    ) {
        EpisodesDataSource(apiService)
    }.flow

}