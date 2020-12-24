package com.meksconway.rickandmorty.ui.adapter.characterdetail

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.meksconway.rickandmorty.common.ordinal
import com.meksconway.rickandmorty.common.viewBinding
import com.meksconway.rickandmorty.data.repository.CharacterDetailItem
import com.meksconway.rickandmorty.data.repository.CharacterDetailItem.*
import com.meksconway.rickandmorty.databinding.*

class CharacterDetailAdapter :
    ListAdapter<CharacterDetailItem, RecyclerView.ViewHolder>(CharacterDetailDiffUtil()) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).ordinal()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PhotoItem.ordinal() -> {
                PhotoVH(parent.viewBinding(ItemChDetailPhotoBinding::inflate))
            }
            TitleItem.ordinal() -> {
                TitleVH(parent.viewBinding(ItemChDetailTitleBinding::inflate))
            }
            DefaultDescriptionItem.ordinal() -> {
                CHDetailDescVH(parent.viewBinding(ItemChDetailDescBinding::inflate))
            }
            OriginItem.ordinal() -> {
                CHDetailOriginVH(parent.viewBinding(ItemChDetailLocationBinding::inflate))
            }
            LocationItem.ordinal() -> {
                CHDetailLocationVH(parent.viewBinding(ItemChDetailLocationBinding::inflate))
            }
            EpisodeItem.ordinal() -> {
                CHDetailEpisodesVH(parent.viewBinding(ItemEpisodesBinding::inflate))
            }
            else -> TitleVH(parent.viewBinding(ItemChDetailTitleBinding::inflate))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            PhotoItem.ordinal() -> {
                (holder as? PhotoVH)?.bind(getItem(position) as PhotoItem)
            }
            TitleItem.ordinal() -> {
                (holder as? TitleVH)?.bind(getItem(position) as TitleItem)
            }
            DefaultDescriptionItem.ordinal() -> {
                (holder as? CHDetailDescVH)?.bind(getItem(position) as DefaultDescriptionItem)
            }
            OriginItem.ordinal() -> {
                (holder as? CHDetailOriginVH)?.bind(getItem(position) as OriginItem)
            }
            LocationItem.ordinal() -> {
                (holder as? CHDetailLocationVH)?.bind(getItem(position) as LocationItem)
            }
            EpisodeItem.ordinal() -> {
                (holder as? CHDetailEpisodesVH)?.bind(getItem(position) as EpisodeItem)
            }
        }
    }
}












