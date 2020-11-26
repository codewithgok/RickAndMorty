package com.meksconway.rickandmorty.data.repository

import com.meksconway.rickandmorty.LocationDetailQuery
import com.meksconway.rickandmorty.common.Resource
import com.meksconway.rickandmorty.common.Status.*
import com.meksconway.rickandmorty.common.resultFlow
import com.meksconway.rickandmorty.data.datasource.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

sealed class LocationDetailItem {

    data class LocationDetailHeaderItem(
        val name: String?,
        val dimension: String?,
        val type: String?
    ) : LocationDetailItem() {
        companion object
    }

    object LocationDetailTitleItem : LocationDetailItem()

    data class LocationDetailCharacterItem(
        val character: LocationDetailQuery.Resident?
    ) : LocationDetailItem() {
        companion object
    }

}



interface LocationRepository {

    suspend fun getSingleLocation(id: String): Flow<Resource<List<LocationDetailItem>>>

}

class LocationRepositoryImpl @Inject constructor(
    private val remote: RemoteDataSource
) : LocationRepository {

    override suspend fun getSingleLocation(id: String): Flow<Resource<List<LocationDetailItem>>> {
        return resultFlow { remote.getLocationDetail(id) }.mapNotNull {
            when (it.status) {
                SUCCESS -> {

                    it.data?.location?.let { loc ->
                        val locationDetailData = mutableListOf<LocationDetailItem>().apply {
                            add(
                                LocationDetailItem.LocationDetailHeaderItem(
                                    name = loc.name,
                                    dimension = loc.dimension,
                                    type = loc.type
                                )
                            )
                            if (!loc.residents.isNullOrEmpty()) {
                                add(LocationDetailItem.LocationDetailTitleItem)
                                loc.residents.mapNotNull { resident ->
                                    add(LocationDetailItem.LocationDetailCharacterItem(resident))
                                }
                            }
                        }

                        Resource.success(locationDetailData)

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