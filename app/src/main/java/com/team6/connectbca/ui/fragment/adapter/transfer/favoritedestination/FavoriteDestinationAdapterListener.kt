package com.team6.connectbca.ui.fragment.adapter.transfer.favoritedestination

import com.team6.connectbca.domain.model.SavedAccountData

interface FavoriteDestinationAdapterListener {
    fun onClickDestination(savedAccount: SavedAccountData)
}