package com.meksconway.rickandmorty.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.meksconway.rickandmorty.base.BaseViewModel
import com.meksconway.rickandmorty.common.Resource
import com.meksconway.rickandmorty.data.repository.LocationDetailItem
import com.meksconway.rickandmorty.data.repository.LocationRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LocationDetailVM
@ViewModelInject constructor(
    private val repository: LocationRepository,
    @Assisted private val stateHandle: SavedStateHandle

) : BaseViewModel() {

    private val _locationData = MutableLiveData<Resource<List<LocationDetailItem>>>()
    val locationData: LiveData<Resource<List<LocationDetailItem>>>
        get() = _locationData


    fun getLocation(id: String?) {
        if (stateHandle.get<Boolean>(IS_CALLED) == true) return
        viewModelScope.launch {
            id?.let {
                repository.getSingleLocation(it).collectLatest { loc ->
                    stateHandle.set(IS_CALLED, true)
                    _locationData.value = loc
                }
            } ?: kotlin.run {
                _locationData.value = Resource.fail("Wrong Location ID")
            }
        }

    }

    companion object {
        const val IS_CALLED = "is_called"
    }


}