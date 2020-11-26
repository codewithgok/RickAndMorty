package com.meksconway.rickandmorty.ui.adapter.locationdetail

import android.graphics.Bitmap
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.meksconway.rickandmorty.common.RMNavigator
import com.meksconway.rickandmorty.common.ordinal
import com.meksconway.rickandmorty.common.viewBinding
import com.meksconway.rickandmorty.data.repository.LocationDetailItem
import com.meksconway.rickandmorty.data.repository.LocationDetailItem.*
import com.meksconway.rickandmorty.databinding.ItemChDetailTitleBinding
import com.meksconway.rickandmorty.databinding.ItemLdHeaderBinding
import com.meksconway.rickandmorty.databinding.ItemLdResidentBinding
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class LocationDetailAdapter : ListAdapter<LocationDetailItem,
        RecyclerView.ViewHolder>(LocationDetailDiffUtil()) {


    override fun getItemViewType(position: Int): Int {
        return getItem(position).ordinal()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LocationDetailHeaderItem.ordinal() -> {
                LocationDetailHeaderVH(parent.viewBinding(ItemLdHeaderBinding::inflate))
            }

            LocationDetailTitleItem.ordinal() -> {
                LocationDetailTitleVH(parent.viewBinding(ItemChDetailTitleBinding::inflate))
            }

            LocationDetailCharacterItem.ordinal() -> {
                LocationDetailCharactersVH(parent.viewBinding(ItemLdResidentBinding::inflate))
            }
            else -> throw IllegalStateException("there is no item type like this bro!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            LocationDetailHeaderItem.ordinal() -> {
                (holder as? LocationDetailHeaderVH)?.bindHeader(getItem(position) as? LocationDetailHeaderItem)
            }
            LocationDetailTitleItem.ordinal() -> {
                (holder as? LocationDetailTitleVH)?.bindTitle()
            }
            LocationDetailCharacterItem.ordinal() -> {
                (holder as? LocationDetailCharactersVH)?.bindResident(getItem(position) as? LocationDetailCharacterItem)
            }
        }
    }
}

class LocationDetailDiffUtil : DiffUtil.ItemCallback<LocationDetailItem>() {
    override fun areItemsTheSame(
        oldItem: LocationDetailItem,
        newItem: LocationDetailItem
    ): Boolean {
        return (oldItem is LocationDetailHeaderItem && newItem is LocationDetailHeaderItem && newItem.name == oldItem.name)
                || (oldItem is LocationDetailCharacterItem && newItem is LocationDetailCharacterItem && oldItem.character?.id == newItem.character?.id)
                || (oldItem is LocationDetailTitleItem && newItem is LocationDetailTitleItem && oldItem == newItem)
    }

    override fun areContentsTheSame(
        oldItem: LocationDetailItem,
        newItem: LocationDetailItem
    ): Boolean {
        return oldItem == newItem
    }
}

class LocationDetailHeaderVH(val binding: ItemLdHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindHeader(model: LocationDetailHeaderItem?) {
        val dimensionText = "Dimension: ${model?.dimension}"
        val planetTypeText = "Planet Type: ${model?.type}"
        binding.txtPlanet.text = model?.name
        binding.txtDimension.text = dimensionText
        binding.txtPlanetType.text = planetTypeText
    }

}

class LocationDetailTitleVH(val binding: ItemChDetailTitleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindTitle() {
        val title = "Characters Played"
        binding.txtTitle.text = title
    }

}

class LocationDetailCharactersVH(val binding: ItemLdResidentBinding) :
    RecyclerView.ViewHolder(binding.root) {


    var model: LocationDetailCharacterItem? = null

    init {
        binding.root.setOnClickListener {
            val id = model?.character?.id
            val name = model?.character?.name
            if (id != null && name != null) {
                RMNavigator.navigateToCharacterDetailFragment(id, name)
            }
        }
    }

    fun bindResident(model: LocationDetailCharacterItem?) {
        this.model = model
        Picasso.get()
            .load(model?.character?.image)
            .fit()
            .centerCrop()
            .networkPolicy(NetworkPolicy.NO_STORE)
            .config(Bitmap.Config.ARGB_8888)
            .into(binding.imgCharacter)

        binding.txtName.text = model?.character?.name
    }

}