package com.meksconway.rickandmorty.ui.adapter

import android.graphics.Bitmap
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.meksconway.rickandmorty.CharactersQuery
import com.meksconway.rickandmorty.common.viewBinding
import com.meksconway.rickandmorty.databinding.ItemCharacterBinding
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class CharactersAdapter(private val clickCharacter: (CharactersQuery.Result?) -> Unit) :
    PagingDataAdapter<CharactersQuery.Result, CharactersAdapter.CharactersVH>(CharactersDiffUtil()) {
    override fun onBindViewHolder(holder: CharactersVH, position: Int) {
        holder.bind(getItem(position))
        holder.binding.root.setOnClickListener {
            clickCharacter.invoke(getItem(position))
        }
    }

    class CharactersVH(val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: CharactersQuery.Result?) {
            Picasso.get()
                .load(model?.image)
                .fit()
                .centerCrop()
                .config(Bitmap.Config.ARGB_8888)
                .into(binding.imgCharacter)
            binding.txtCharacter.text = model?.name
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharactersVH {
        return CharactersVH(parent.viewBinding(ItemCharacterBinding::inflate))
    }


}

class CharactersDiffUtil : DiffUtil.ItemCallback<CharactersQuery.Result>() {
    override fun areItemsTheSame(
        oldItem: CharactersQuery.Result,
        newItem: CharactersQuery.Result
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: CharactersQuery.Result,
        newItem: CharactersQuery.Result
    ): Boolean {
        return oldItem == newItem
    }


}