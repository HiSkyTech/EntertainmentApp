    package com.urdubolo.com.pk.Interfaces
    
    import android.graphics.ColorSpace.Model
    import com.urdubolo.com.pk.Model.*
    import okhttp3.MultipartBody
    import okhttp3.RequestBody
    import retrofit2.Call
    import retrofit2.http.*
    
    data class DramaIdRequest(val drama_id: Int)
    data class SeasonIdRequest(val season_id: Int)
    data class UserIdVideoRequest(val user_id: Int)
    data class UserLogoutId(val id: Int)
    data class UserLoginRequest(val email: String, val password: String)
    
    interface ApiInterFace {
        @GET("get_dramas.php")
        fun getAllDramas(): Call<ModelDrama>
    
        @GET("get_seasons.php")
        fun getAllSeasons(): Call<ModelSeason>
    
        @GET("fetch_videos.php")
        fun getAllEpisodes(): Call<ModelAssignedVideo>
    
        @GET("fetch_users.php")
        fun getAllUsers(): Call<ModelUser>
    
        @POST("get_video.php")
        fun getSpecifiedAssignedVideo(@Body userIdVideoRequest: UserIdVideoRequest): Call<ModelAssignedVideo>

        @POST("logout_api.php")
        fun logoutUser(@Body userLogoutId: UserLogoutId): Call<ModelLogoutResponse>

        @POST("fetch_seasons.php")
        fun getDramaSeasons(@Body dramaIdRequest: DramaIdRequest): Call<List<ModelSeasonItem>>
    
        @POST("fetch_episodes.php")
        fun getSeasonsEpisodes(@Body seasonIdRequest: SeasonIdRequest): Call<List<ModelEpisodeItem>>
    
        @POST("api_login.php")
        fun loginUser(@Body userLoginRequest: UserLoginRequest): Call<ModelUserResponse>
    
        @Multipart
        @POST("insert_user_api.php")
        fun signUpUser(
            @Part("username") username: RequestBody,
            @Part("email") email: RequestBody,
            @Part("password") password: RequestBody,
            @Part profile_image: MultipartBody.Part
        ): Call<ModelUserSignUpResponse>
    }
