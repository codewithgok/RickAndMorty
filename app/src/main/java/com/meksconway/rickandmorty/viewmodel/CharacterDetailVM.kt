package com.meksconway.rickandmorty.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.meksconway.rickandmorty.base.BaseViewModel
import com.meksconway.rickandmorty.common.Resource
import com.meksconway.rickandmorty.data.repository.CharacterDetailItem
import com.meksconway.rickandmorty.data.repository.CharacterRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CharacterDetailVM
@ViewModelInject constructor(
    private val repository: CharacterRepository,
    @Assisted private val stateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _characterData = MutableLiveData<Resource<List<CharacterDetailItem>>>()
    val characterData: LiveData<Resource<List<CharacterDetailItem>>>
        get() = _characterData


    fun getSingleCharacter(id: String) {
        stateHandle.set(CHARACTER_ID, id)
        if (stateHandle.get<Boolean>(IS_CALLED) == true) return
        viewModelScope.launch {
            stateHandle.get<String>(CHARACTER_ID)?.let {
                repository.getSingleCharacter(it).collectLatest { ch ->
                    _characterData.value = ch
                    stateHandle.set(IS_CALLED, true)
                }
            }
        }
    }

    companion object {
        const val IS_CALLED = "is_called"
        const val CHARACTER_ID = "character_id"
    }


}