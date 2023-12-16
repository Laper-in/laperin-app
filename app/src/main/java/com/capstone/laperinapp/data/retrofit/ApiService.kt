package com.capstone.laperinapp.data.retrofit

import com.capstone.laperinapp.data.response.BookmarkResponse
import com.capstone.laperinapp.data.response.DataRecipes
import com.capstone.laperinapp.data.response.DataUser
import com.capstone.laperinapp.data.response.DetailRecipesResponses
import com.capstone.laperinapp.data.response.DetailUserResponse
import com.capstone.laperinapp.data.response.LoginResponse
import com.capstone.laperinapp.data.response.RecipeResponses
import com.capstone.laperinapp.data.response.RecipesResponses
import com.capstone.laperinapp.data.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.math.BigInteger

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
    suspend fun getDetailUser(
        @Path("id") id: String
    ): Response<DataUser>

    @FormUrlEncoded
    @PATCH("users/{id}")
    suspend fun updateDetailUser(
        @Path("id") id: String,
        @Field("email") email: String,
        @Field("fullname") fullname: String,
        @Field("alamat") alamat: String,
        @Field("telephone") telephone: BigInteger
    ):  Response<DetailUserResponse>

    @GET("recipes")
    suspend fun getAllRecipes(
        @Query("page") page: Int,
        @Query("pageSize") size: Int
    ): RecipeResponses

    @GET("recipes/{id}")
    suspend fun getDetailRecipes(
        @Path("id") id: String
    ): Response<DetailRecipesResponses>

    @GET("bookmarks/{id}")
    suspend fun getBookmarks(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("pageSize") size: Int
    ): BookmarkResponse
}