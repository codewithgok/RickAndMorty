package com.meksconway.rickandmorty.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.meksconway.rickandmorty.EpisodesQuery
import com.meksconway.rickandmorty.common.RMNavigator
import com.meksconway.rickandmorty.common.ordinal
import com.meksconway.rickandmorty.common.viewBinding
import com.meksconway.rickandmorty.databinding.ItemEpisodesBinding
import com.meksconway.rickandmorty.databinding.ItemEpisodesHeaderBinding
import com.meksconway.rickandmorty.ui.adapter.EpisodeItemType.Episode
import com.meksconway.rickandmorty.ui.adapter.EpisodeItemType.Header


sealed class EpisodeItemType {
    data class Episode(val episodes: EpisodesQuery.Result) : EpisodeItemType() {
        companion object
    }

    data class Header(val seasonName: String) : EpisodeItemType() {
        companion object
    }
}

class EpisodesAdapter : PagingDataAdapter<EpisodeItemType,
        RecyclerView.ViewHolder>(EpisodesDiffUtil()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = getItem(position)
        if (model is EpisodeItemType) {
            when (model) {
                is Episode -> (holder as? EpisodesVH)?.bind(model.episodes)
                is Header -> {
                    (holder as? EpisodeHeaderVH)?.binding?.txtSeason?.text = model.seasonName
                }
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.ordinal() ?: -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Header.ordinal() -> EpisodeHeaderVH(parent.viewBinding(ItemEpisodesHeaderBinding::inflate))
            Episode.ordinal() -> EpisodesVH(parent.viewBinding(ItemEpisodesBinding::inflate))
            else -> throw IllegalStateException()
        }
    }

}

class EpisodesVH(private val binding: ItemEpisodesBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private var model: EpisodesQuery.Result? = null

    init {
        binding.root.setOnClickListener {
            RMNavigator.navigateToEpisodeDetailFragment(
                episodeId = model?.id,
                episodeName = model?.episode
            )
        }
    }

    fun bind(model: EpisodesQuery.Result?) {
        this.model = model
        val airDateText = "Air Date: ${model?.air_date}"
        binding.txtAirDate.text = airDateText
        binding.txtName.text = model?.name
        binding.txtEpisode.text = model?.episode
    }
}

class EpisodeHeaderVH(val binding: ItemEpisodesHeaderBinding) :
    RecyclerView.ViewHolder(binding.root)

class EpisodesDiffUtil : DiffUtil.ItemCallback<EpisodeItemType>() {
    override fun areItemsTheSame(oldItem: EpisodeItemType, newItem: EpisodeItemType): Boolean {
        return (oldItem is Episode &&
                newItem is Episode &&
                oldItem.episodes.id == newItem.episodes.id) ||
                (oldItem is Header &&
                        newItem is Header &&
                        oldItem.seasonName == newItem.seasonName)
    }

    override fun areContentsTheSame(oldItem: EpisodeItemType, newItem: EpisodeItemType): Boolean {
        return oldItem == newItem
    }
}