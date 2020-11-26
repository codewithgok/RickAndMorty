package com.meksconway.rickandmorty.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.meksconway.rickandmorty.base.BaseViewModel
import com.meksconway.rickandmorty.common.Resource
import com.meksconway.rickandmorty.data.repository.EpisodeDetailItem
import com.meksconway.rickandmorty.data.repository.EpisodeRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EpisodeDetailVM
@ViewModelInject constructor(
    private val repository: EpisodeRepository,
    @Assisted private val stateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _episodeData = MutableLiveData<Resource<List<EpisodeDetailItem>>>()
    val characterData: LiveData<Resource<List<EpisodeDetailItem>>>
        get() = _episodeData

    fun getSingleEpisode(id: String) {
        stateHandle.set(EPISODE_ID, id)
        if (stateHandle.get<Boolean>(IS_CALLED) == true) return
        viewModelScope.launch {
            stateHandle.get<String>(EPISODE_ID)?.let {
                repository.getSingleEpisode(it).collectLatest { ch ->
                    _episodeData.value = ch
                    stateHandle.set(IS_CALLED, true)
                }
            }
        }
    }


    companion object {
        const val IS_CALLED = "is_called"
        const val EPISODE_ID = "character_id"
    }

}