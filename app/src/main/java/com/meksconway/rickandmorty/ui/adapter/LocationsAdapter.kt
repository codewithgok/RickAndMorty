package com.meksconway.rickandmorty.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.meksconway.rickandmorty.LocationsQuery
import com.meksconway.rickandmorty.common.RMNavigator
import com.meksconway.rickandmorty.common.viewBinding
import com.meksconway.rickandmorty.databinding.ItemLocationsBinding

class LocationsAdapter :
    PagingDataAdapter<LocationsQuery.Result,
            LocationsVH>(LocationsDiffUtil()) {

    override fun onBindViewHolder(holder: LocationsVH, position: Int) {
        holder.bind(getItem(position))
        holder.binding.root.setOnClickListener {
            val model = getItem(position)
            RMNavigator.navigateToLocationDetailFragment(
                model?.id,
                model?.name
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsVH {
        return LocationsVH(parent.viewBinding(ItemLocationsBinding::inflate))
    }
}


class LocationsVH(val binding: ItemLocationsBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(model: LocationsQuery.Result?) {
        val dimensionText = "Dimension: ${model?.dimension}"
        val typeText = "Type: ${model?.type}"
        binding.txtDimension.text = dimensionText
        binding.txtPlanetType.text = typeText
        binding.txtPlanet.text = model?.name
    }
}

class LocationsDiffUtil : DiffUtil.ItemCallback<LocationsQuery.Result>() {
    override fun areItemsTheSame(
        oldItem: LocationsQuery.Result,
        newItem: LocationsQuery.Result
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: LocationsQuery.Result,
        newItem: LocationsQuery.Result
    ): Boolean {
        return oldItem == newItem
    }
}