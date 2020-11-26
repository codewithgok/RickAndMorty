package com.meksconway.rickandmorty.data.repository

import com.meksconway.rickandmorty.CharacterDetailQuery
import com.meksconway.rickandmorty.common.Resource
import com.meksconway.rickandmorty.common.Status.*
import com.meksconway.rickandmorty.common.resultFlow
import com.meksconway.rickandmorty.data.datasource.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

sealed class CharacterDetailItem {

    class TitleItem(val title: String?) :
        CharacterDetailItem() {
        companion object
    }

    data class DefaultDescriptionItem(val desc: String?) :
        CharacterDetailItem() {
        companion object
    }

    data class PhotoItem(val image: String?, val name: String?, val status: String?) :
        CharacterDetailItem() {
        companion object
    }

    data class LocationItem(val location: CharacterDetailQuery.Location?) :
        CharacterDetailItem() {
        companion object
    }

    data class OriginItem(val location: CharacterDetailQuery.Origin?) :
        CharacterDetailItem() {
        companion object
    }

    data class EpisodeItem(val episodes: CharacterDetailQuery.Episode) :
        CharacterDetailItem() {
        companion object
    }
}

class CharacterRepositoryImpl @Inject constructor(
    private val remote: RemoteDataSource
) : CharacterRepository {

    override suspend fun getSingleCharacter(id: String):
            Flow<Resource<List<CharacterDetailItem>>> {

        return resultFlow { remote.getCharacterDetail(id) }.mapNotNull {
            when (it.status) {
                SUCCESS -> {
                    it.data?.character?.let { ch ->

                        val characterDetailListData = mutableListOf<CharacterDetailItem>().apply {
                            add(
                                CharacterDetailItem.PhotoItem(
                                    image = ch.image,
                                    name = ch.name,
                                    status = ch.status
                                )
                            )


                            if (!ch.species.isNullOrBlank()) {
                                add(
                                    CharacterDetailItem.TitleItem(
                                        title = "Species"
                                    )
                                )
                                add(
                                    CharacterDetailItem.DefaultDescriptionItem(
                                        desc = ch.species
                                    )
                                )
                            }

                            if (!ch.gender.isNullOrBlank()) {
                                add(
                                    CharacterDetailItem.TitleItem(
                                        title = "Gender"
                                    )
                                )
                                add(
                                    CharacterDetailItem.DefaultDescriptionItem(
                                        desc = ch.gender
                                    )
                                )
                            }

                            if (!ch.type.isNullOrBlank()) {
                                add(
                                    CharacterDetailItem.TitleItem(
                                        title = "Type"
                                    )
                                )
                                add(
                                    CharacterDetailItem.DefaultDescriptionItem(
                                        desc = ch.type
                                    )
                                )
                            }

                            add(
                                CharacterDetailItem.TitleItem(
                                    title = "First Seen In"
                                )
                            )

                            add(
                                CharacterDetailItem.OriginItem(
                                    location = ch.origin
                                )
                            )

                            add(
                                CharacterDetailItem.TitleItem(
                                    title = "Last Known Location"
                                )
                            )

                            add(
                                CharacterDetailItem.LocationItem(
                                    location = ch.location
                                )
                            )

                            add(
                                CharacterDetailItem.TitleItem(
                                    title = "Episodes Played"
                                )
                            )

                            ch.episode?.mapNotNull { es ->
                                es?.let {
                                    this.add(
                                        CharacterDetailItem.EpisodeItem(
                                            episodes = es
                                        )
                                    )
                                }
                            }




                        }

                        Resource.success(characterDetailListData)
                    } ?: Resource.fail(it.errorMessage)
                }
                FAIL -> {
                    Resource.fail(it.errorMessage)
                }
                LOADING -> {
                    Resource.loading()
                }
            }
        }
    }


}


