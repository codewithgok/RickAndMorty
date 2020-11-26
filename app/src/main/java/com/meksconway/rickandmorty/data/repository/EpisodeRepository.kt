package com.meksconway.rickandmorty.data.repository

import com.meksconway.rickandmorty.EpisodeDetailQuery
import com.meksconway.rickandmorty.common.Resource
import com.meksconway.rickandmorty.common.Status
import com.meksconway.rickandmorty.common.resultFlow
import com.meksconway.rickandmorty.data.datasource.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

sealed class EpisodeDetailItem {

    data class EpisodeDetailHeaderItem(
        val id: String?,
        val name: String?,
        val airDate: String?,
        val episode: String?
    ) : EpisodeDetailItem() {
        companion object
    }

    object EpisodeDetailTitleItem : EpisodeDetailItem()

    data class EpisodeDetailCharacterItem(
        val character: EpisodeDetailQuery.Character
    ) : EpisodeDetailItem() {
        companion object
    }

}


interface EpisodeRepository {
    fun getSingleEpisode(id: String): Flow<Resource<List<EpisodeDetailItem>>>
}

class EpisodeRepositoryImpl @Inject constructor(
    private val remote: RemoteDataSource
) : EpisodeRepository {

    override fun getSingleEpisode(id: String): Flow<Resource<List<EpisodeDetailItem>>> {
        return resultFlow { remote.getEpisodesDetail(id) }.mapNotNull {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.episode?.let { ep ->
                        val episodeData = mutableListOf<EpisodeDetailItem>().apply {
                            add(
                                EpisodeDetailItem.EpisodeDetailHeaderItem(
                                    id = ep.id,
                                    name = ep.name,
                                    airDate = ep.air_date,
                                    episode = ep.episode
                                )
                            )
                            add(EpisodeDetailItem.EpisodeDetailTitleItem)
                            ep.characters?.mapNotNull { ch ->
                                ch?.let {
                                    add(EpisodeDetailItem.EpisodeDetailCharacterItem(ch))
                                }
                            }
                        }
                        Resource.success(episodeData)
                    } ?: Resource.fail(it.errorMessage)
                }
                Status.FAIL -> {
                    Resource.fail(it.errorMessage)
                }
                Status.LOADING -> {
                    Resource.loading()
                }
            }
        }
    }
}

