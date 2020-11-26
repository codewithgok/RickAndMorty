package com.meksconway.rickandmorty.common

import com.meksconway.rickandmorty.ui.fragment.CharacterDetailFragment
import com.meksconway.rickandmorty.ui.fragment.EpisodeDetailFragment
import com.meksconway.rickandmorty.ui.fragment.LocationDetailFragment
import com.trendyol.medusalib.navigator.MultipleStackNavigator

object RMNavigator {

    private var navigator: MultipleStackNavigator? = null

    fun register(navigator: MultipleStackNavigator?) {
        this.navigator = navigator
    }

    fun popBack() {
        navigator?.safeBack()
    }

    fun backToRoot(tabIndex: Int, completion: (Boolean) -> Unit) {
        if (navigator?.hasOnlyRoot(tabIndex) == false) {
            navigator?.reset(tabIndex)
            completion(false)
        } else {
            completion(true)
        }
    }

    fun navigateToCharacterDetailFragment(characterId: String?, characterName: String?) {
        navigator?.start(
            CharacterDetailFragment.newInstance(
                characterId = characterId,
                characterName = characterName
            )
        )
    }

    fun navigateToEpisodeDetailFragment(episodeId: String?, episodeName: String?) {
        navigator?.start(
            EpisodeDetailFragment.newInstance(
                episodeId = episodeId,
                episodeName = episodeName
            )
        )
    }

    fun navigateToLocationDetailFragment(locationId: String?, locationName: String?) {
        navigator?.start(
            LocationDetailFragment.newInstance(
                locId = locationId,
                locName = locationName
            )
        )
    }


    fun unregister() {
        navigator = null
    }

}

