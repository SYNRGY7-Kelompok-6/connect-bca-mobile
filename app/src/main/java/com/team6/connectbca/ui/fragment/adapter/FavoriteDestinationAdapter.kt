package com.team6.connectbca.ui.fragment.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team6.connectbca.databinding.ItemFavoriteDestinationBinding
import com.team6.connectbca.domain.model.SavedAccountData

class FavoritesDestinationAdapter(
    private var favorites: List<SavedAccountData>,
    private val onItemClick: (SavedAccountData) -> Unit
) : RecyclerView.Adapter<FavoritesDestinationAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteDestinationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favorites[position])
    }

    override fun getItemCount(): Int = favorites.size

    fun updateFavorites(newFavorites: List<SavedAccountData>) {
        favorites = newFavorites
        notifyDataSetChanged()
    }

    inner class FavoriteViewHolder(private val binding: ItemFavoriteDestinationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favorite: SavedAccountData) {
            val nickname = getAcronym(favorite.beneficiaryAccountName!!)

            binding.apply {
                tvName.text = favorite.beneficiaryAccountName
                tvBankInfo.text = "BCA"
                tvAccountNumber.text = favorite.beneficiaryAccountNumber
                logoText.text = nickname

                root.setOnClickListener { onItemClick(favorite) }
            }
        }
    }

    private fun getAcronym(word: String) : String {
        return if (word.length >= 2) {
            val trimmedString = word.substring(2,word.length)
            word.replace(trimmedString, "").uppercase()
        } else {
            word.uppercase()
        }
    }
}