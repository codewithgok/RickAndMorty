package com.meksconway.rickandmorty.common

import com.meksconway.rickandmorty.base.BaseFragment
import com.meksconway.rickandmorty.ui.fragment.CharacterDetailFragment
import com.meksconway.rickandmorty.ui.fragment.EpisodeDetailFragment
import com.meksconway.rickandmorty.ui.fragment.LocationDetailFragment
import com.trendyol.medusalib.navigator.MultipleStackNavigator

object RMNavigator {

    private var navigator: MultipleStackNavigator? = null
    private var backHandler: BackHandler? = null

    interface BackHandler {
        val canBack: Boolean
        fun onBackPressed()
    }

    fun register(navBar: BackHandler) {
        this.backHandler = navBar
    }

    fun register(navigator: MultipleStackNavigator?) {
        this.navigator = navigator
    }

    fun popBack(block: () -> Unit = {}) {
        if (backHandler?.canBack == false) {
            backHandler?.onBackPressed()
            return
        }
        navigator?.safeBackWithCompletion(block)
    }

    fun backToRoot(tabIndex: Int, completion: (Boolean) -> Unit) {
        if (navigator?.hasOnlyRoot(tabIndex) == false) {
            navigator?.reset(tabIndex)
            completion(false)
        } else {
            completion(true)
        }
    }

    private fun <T : BaseFragment<*>> navigate(factory: () -> T) {
        if (backHandler?.canBack == false) {
            backHandler?.onBackPressed()
        }

        navigator?.start(factory.invoke())
    }

    fun navigateToCharacterDetailFragment(characterId: String?, characterName: String?) {
        navigate {
            CharacterDetailFragment.newInstance(
                characterId = characterId,
                characterName = characterName
            )
        }
    }

    fun navigateToEpisodeDetailFragment(episodeId: String?, episodeName: String?) {
        navigate {
            EpisodeDetailFragment.newInstance(
                episodeId = episodeId,
                episodeName = episodeName
            )
        }
    }

    fun navigateToLocationDetailFragment(locationId: String?, locationName: String?) {
        navigate {
            LocationDetailFragment.newInstance(
                locId = locationId,
                locName = locationName
            )
        }
    }


    fun unregister() {
        navigator = null
        backHandler = null
    }

}

