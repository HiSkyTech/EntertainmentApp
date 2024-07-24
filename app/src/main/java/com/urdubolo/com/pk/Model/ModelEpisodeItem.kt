package com.urdubolo.com.pk.Model

data class ModelEpisodeItem(
    val created_at: String,
    val description: String,
    val download_access: String,
    val episode_number: Int,
    val id: Int,
    val privacy: String,
    val season_id: Int,
    val thumbnail: Any,
    val video_path: String
)