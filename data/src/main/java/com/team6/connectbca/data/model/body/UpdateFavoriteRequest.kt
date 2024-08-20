package com.team6.connectbca.data.model.body

import com.google.gson.annotations.SerializedName

data class UpdateFavoriteRequest(
    @SerializedName("isFavorite")
    val isFavorite: Boolean
)