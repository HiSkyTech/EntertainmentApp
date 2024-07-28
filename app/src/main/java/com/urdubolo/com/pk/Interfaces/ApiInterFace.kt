package com.urdubolo.com.pk.Interfaces

import com.urdubolo.com.pk.Model.ModelDrama
import com.urdubolo.com.pk.Model.ModelEpisode
import com.urdubolo.com.pk.Model.ModelEpisodeItem
import com.urdubolo.com.pk.Model.ModelSeasonItem
import com.urdubolo.com.pk.Model.ModelUser
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class DramaIdRequest(val drama_id: Int)
data class SeasonIdRequest(val season_id: Int)

interface ApiInterFace {
    @GET("get_dramas.php")
    fun getAllDramas(): Call<ModelDrama>

    @GET("fetch_episodes.php")
    fun getAllEpisodes(): Call<ModelEpisode>

    @GET("fetch_users.php")
    fun getAllUsers(): Call<ModelUser>

    @POST("fetch_seasons.php")
    fun getDramaSeasons(@Body dramaIdRequest: DramaIdRequest): Call<List<ModelSeasonItem>>



    @POST("fetch_episodes.php")
    fun getSeasonsEpisodes(@Body seasonIdRequest: SeasonIdRequest): Call<List<ModelEpisodeItem>>
}
