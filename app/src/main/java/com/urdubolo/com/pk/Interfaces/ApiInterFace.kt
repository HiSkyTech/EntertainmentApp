package com.urdubolo.com.pk.Interfaces

import com.urdubolo.com.pk.Model.ModelDrama
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterFace {

@GET
    fun getAllDramas(): Call<ModelDrama>

}

