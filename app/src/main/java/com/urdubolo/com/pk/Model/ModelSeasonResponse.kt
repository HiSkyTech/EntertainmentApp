package com.urdubolo.com.pk.Model

data class ModelSeasonResponse(
    val id: Int,
    val drama_id: Int,
    val season_number: Int,
    val total_episodes: Int,
    val thumbnail: String,
    val created_at: String
)
