package com.meksconway.rickandmorty.ui.adapter.characterdetail

import androidx.recyclerview.widget.DiffUtil
import com.meksconway.rickandmorty.data.repository.CharacterDetailItem

class CharacterDetailDiffUtil : DiffUtil.ItemCallback<CharacterDetailItem>() {
    override fun areItemsTheSame(
        oldItem: CharacterDetailItem,
        newItem: CharacterDetailItem
    ): Boolean {
        return (oldItem is CharacterDetailItem.EpisodeItem && newItem is CharacterDetailItem.EpisodeItem && oldItem.episodes == newItem.episodes)
                || (oldItem is CharacterDetailItem.TitleItem && newItem is CharacterDetailItem.TitleItem && oldItem.title == newItem.title)
                || (oldItem is CharacterDetailItem.LocationItem && newItem is CharacterDetailItem.LocationItem && oldItem.location?.id == newItem.location?.id)
                || (oldItem is CharacterDetailItem.PhotoItem && newItem is CharacterDetailItem.PhotoItem && oldItem.name == newItem.name)
                || (oldItem is CharacterDetailItem.DefaultDescriptionItem && newItem is CharacterDetailItem.DefaultDescriptionItem && oldItem.desc == newItem.desc)
                || (oldItem is CharacterDetailItem.OriginItem && newItem is CharacterDetailItem.OriginItem && oldItem.location?.id == newItem.location?.id)

    }

    override fun areContentsTheSame(
        oldItem: CharacterDetailItem,
        newItem: CharacterDetailItem
    ): Boolean {
        return oldItem == newItem
    }
}