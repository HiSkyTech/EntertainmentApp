package com.urdubolo.com.pk.Model

data class Video(
    val created_at: String,
    val description: String,
    val download_access: Any,
    val episode_number: Int,
    val id: Int,
    val privacy: String,
    val season_id: Int,
    val thumbnail: Any,
    val video_path: String
)