package com.meksconway.rickandmorty.ui.adapter.episodedetail

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.meksconway.rickandmorty.R
import com.meksconway.rickandmorty.common.RMNavigator
import com.meksconway.rickandmorty.common.ordinal
import com.meksconway.rickandmorty.common.viewBinding
import com.meksconway.rickandmorty.data.repository.EpisodeDetailItem
import com.meksconway.rickandmorty.databinding.ItemChDetailTitleBinding
import com.meksconway.rickandmorty.databinding.ItemEdHeaderBinding
import com.meksconway.rickandmorty.databinding.ItemLdResidentBinding
import com.squareup.picasso.Picasso

class EpisodeDetailAdapter : ListAdapter<EpisodeDetailItem,
        RecyclerView.ViewHolder>(EpisodeDetailDiffUtil()) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).ordinal()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            EpisodeDetailItem.EpisodeDetailHeaderItem.ordinal() -> {
                EpisodeDetailHeaderVH(parent.viewBinding(ItemEdHeaderBinding::inflate))
            }
            EpisodeDetailItem.EpisodeDetailTitleItem.ordinal() -> {
                EpisodeDetailTitleVH(parent.viewBinding(ItemChDetailTitleBinding::inflate))
            }
            EpisodeDetailItem.EpisodeDetailCharacterItem.ordinal() -> {
                EpisodeDetailCharactersVH(parent.viewBinding(ItemLdResidentBinding::inflate))
            }

            else -> throw IllegalStateException("there is no item type like this bro!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val modelItem = getItem(position)
        when (getItemViewType(position)) {
            EpisodeDetailItem.EpisodeDetailHeaderItem.ordinal() -> {
                (holder as? EpisodeDetailHeaderVH)?.bindHeader(modelItem as EpisodeDetailItem.EpisodeDetailHeaderItem)
            }
            EpisodeDetailItem.EpisodeDetailTitleItem.ordinal() -> {
                (holder as? EpisodeDetailTitleVH)?.bindTitle()
            }
            EpisodeDetailItem.EpisodeDetailCharacterItem.ordinal() -> {
                (holder as? EpisodeDetailCharactersVH)?.bindCharacters(modelItem as EpisodeDetailItem.EpisodeDetailCharacterItem)
            }
        }
    }
}


class EpisodeDetailDiffUtil : DiffUtil.ItemCallback<EpisodeDetailItem>() {
    override fun areItemsTheSame(oldItem: EpisodeDetailItem, newItem: EpisodeDetailItem): Boolean {
        return (oldItem is EpisodeDetailItem.EpisodeDetailHeaderItem && newItem is EpisodeDetailItem.EpisodeDetailHeaderItem && oldItem.id == newItem.id)
                || (oldItem is EpisodeDetailItem.EpisodeDetailTitleItem && newItem is EpisodeDetailItem.EpisodeDetailTitleItem && oldItem == newItem)
                || (oldItem is EpisodeDetailItem.EpisodeDetailCharacterItem && newItem is EpisodeDetailItem.EpisodeDetailCharacterItem && oldItem.character.id == newItem.character.id)
    }

    override fun areContentsTheSame(
        oldItem: EpisodeDetailItem,
        newItem: EpisodeDetailItem
    ): Boolean {
        return oldItem == newItem
    }
}

class EpisodeDetailHeaderVH(val binding: ItemEdHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindHeader(model: EpisodeDetailItem.EpisodeDetailHeaderItem) {
        Picasso.get()
            .load(R.drawable.rm_episode)
            .centerCrop()
            .fit()
            .into(binding.imgEpisodeHeader)
        val airDateText = "Air Date: ${model.airDate}"
        binding.txtAirDate.text = airDateText
        val episodeNameText = "Episode: ${model.episode}"
        binding.txtEpisode.text = episodeNameText
        binding.txtEpisodeName.text = model.name
    }
}

class EpisodeDetailTitleVH(val binding: ItemChDetailTitleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindTitle() {
        binding.txtTitle.text = binding.root.context.getString(R.string.ed_title_section)
    }

}

class EpisodeDetailCharactersVH(val binding: ItemLdResidentBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private var model: EpisodeDetailItem.EpisodeDetailCharacterItem? = null

    init {
        binding.root.setOnClickListener {
            RMNavigator.navigateToCharacterDetailFragment(
                characterId = model?.character?.id,
                characterName = model?.character?.name
            )
        }
    }

    fun bindCharacters(model: EpisodeDetailItem.EpisodeDetailCharacterItem) {
        this.model = model
        Picasso.get()
            .load(model.character.image)
            .fit()
            .centerCrop()
            .into(binding.imgCharacter)
        binding.txtName.text = model.character.name
    }

}