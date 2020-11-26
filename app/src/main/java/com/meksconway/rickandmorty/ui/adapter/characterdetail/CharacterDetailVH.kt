package com.meksconway.rickandmorty.ui.adapter.characterdetail

import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.meksconway.rickandmorty.common.RMNavigator
import com.meksconway.rickandmorty.data.repository.CharacterDetailItem
import com.meksconway.rickandmorty.databinding.*
import com.squareup.picasso.Picasso


sealed class CharacterDetailVH(val viewBinding: ViewBinding) :
    RecyclerView.ViewHolder(viewBinding.root)


class PhotoVH(val binding: ItemChDetailPhotoBinding) : CharacterDetailVH(binding) {

    fun bind(model: CharacterDetailItem.PhotoItem) {
        binding.txtName.text = model.name
        binding.txtStatus.text = model.status
        Picasso.get().load(model.image).into(binding.imgCharacter)
    }

}

class TitleVH(val binding: ItemChDetailTitleBinding) :
    CharacterDetailVH(binding) {

    fun bind(model: CharacterDetailItem.TitleItem) {
        binding.txtTitle.text = model.title
    }

}

class CHDetailDescVH(val binding: ItemChDetailDescBinding) :
    CharacterDetailVH(binding) {

    fun bind(model: CharacterDetailItem.DefaultDescriptionItem) {
        binding.txtDesc.text = model.desc
    }
}

class CHDetailOriginVH(val binding: ItemChDetailLocationBinding) :
    CharacterDetailVH(binding) {

    private var model: CharacterDetailItem.OriginItem? = null

    init {
        binding.btnLocation.setOnClickListener {
            RMNavigator.navigateToLocationDetailFragment(
                locationId = model?.location?.id,
                locationName = model?.location?.name
            )
        }
    }

    fun bind(model: CharacterDetailItem.OriginItem) {
        this.model = model
        binding.btnLocation.text = model.location?.name
    }
}

class CHDetailLocationVH(val binding: ItemChDetailLocationBinding) :
    CharacterDetailVH(binding) {

    private var model: CharacterDetailItem.LocationItem? = null

    init {
        binding.btnLocation.setOnClickListener {
            RMNavigator.navigateToLocationDetailFragment(
                locationId = model?.location?.id,
                locationName = model?.location?.name
            )
        }
    }

    fun bind(model: CharacterDetailItem.LocationItem) {
        this.model = model
        binding.btnLocation.text = model.location?.name
    }
}

class CHDetailEpisodesVH(private val binding: ItemEpisodesBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private var model: CharacterDetailItem.EpisodeItem? = null

    init {
        binding.root.setOnClickListener {
            RMNavigator.navigateToEpisodeDetailFragment(
                episodeName = model?.episodes?.episode,
                episodeId = model?.episodes?.id
            )
        }
    }

    fun bind(model: CharacterDetailItem.EpisodeItem) {
        this.model = model
        binding.txtAirDate.isGone = true
        binding.txtName.text = model.episodes.name
        binding.txtEpisode.text = model.episodes.episode
    }
}

