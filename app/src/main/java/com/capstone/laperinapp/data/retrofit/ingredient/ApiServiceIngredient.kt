package com.capstone.laperinapp.data.retrofit.ingredient

import com.capstone.laperinapp.data.response.ResponseSearchIngredient
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiServiceIngredient {

    @POST("recommender")
    suspend fun searchScan(
        @Query("user_input") ingredient: String
    ): Response<ResponseSearchIngredient>
}