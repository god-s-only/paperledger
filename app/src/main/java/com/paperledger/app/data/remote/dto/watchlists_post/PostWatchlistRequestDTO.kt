package com.paperledger.app.data.remote.dto.watchlists_post


import com.google.gson.annotations.SerializedName

data class PostWatchlistRequestDTO(
    @SerializedName("name")
    val name: String,
    @SerializedName("symbols")
    val symbols: List<String>
)