package com.team6.connectbca.ui.fragment.adapter.transfer.favoritedestination

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.team6.connectbca.R
import com.team6.connectbca.databinding.ItemFavoriteDestinationBinding
import com.team6.connectbca.domain.model.SavedAccountData

class FavoritesDestinationAdapter(
    private val favoriteDestinationAdapterListener: FavoriteDestinationAdapterListener
) : ListAdapter<SavedAccountData, FavoritesDestinationAdapter.FavoriteViewHolder>(
    FavoriteDestinationDiffUtil()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteDestinationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int = currentList.size

    inner class FavoriteViewHolder(private val binding: ItemFavoriteDestinationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favorite: SavedAccountData) {
            val nickname = getAcronym(favorite.beneficiaryAccountName!!)

            binding.apply {
                tvName.text = favorite.beneficiaryAccountName
                tvBankInfo.text = "BCA"
                tvAccountNumber.text = favorite.beneficiaryAccountNumber
                tvAccountNumber.contentDescription = favorite.beneficiaryAccountNumber?.split("")?.joinToString(" ")
                logoText.text = nickname
                if (favorite.favorite == true) {
                    heartIcon.setImageResource(R.drawable.ic_heart)
                } else {
                    heartIcon.setImageResource(R.drawable.ic_heart_outline)
                }
                root.setOnClickListener { favoriteDestinationAdapterListener.onClickDestination(favorite) }
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