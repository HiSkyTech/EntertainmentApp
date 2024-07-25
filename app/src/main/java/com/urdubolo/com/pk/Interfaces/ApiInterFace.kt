package com.urdubolo.com.pk.Interfaces

import com.urdubolo.com.pk.Model.ModelDrama
import com.urdubolo.com.pk.Model.ModelUser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterFace {

    @GET("get_dramas.php")
    fun getAllDramas( ): Call<ModelDrama>

    @GET ("fetch_users.php")
    fun getAllUsers():Call<ModelUser>

}

