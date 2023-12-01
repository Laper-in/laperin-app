package com.capstone.laperinapp.data.retrofit

import com.capstone.laperinapp.data.response.DetailUserResponse
import com.capstone.laperinapp.data.response.LoginResponse
import com.capstone.laperinapp.data.response.RegisterResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("users/signin")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("users/signup")
    suspend fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,

    ): Response<RegisterResponse>



    @GET("users/{id}")
    fun getDetailUser(
        @Field("id") id: String
    ): Call<DetailUserResponse>

    @FormUrlEncoded
    @PATCH("users/{id}")
    fun updateDetailUser(
        @Field("id") id: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<DetailUserResponse>

}