package com.capstone.laperinapp.data.retrofit

import com.capstone.laperinapp.data.response.AddDonationResponse
import com.capstone.laperinapp.data.response.BookmarkResponse
import com.capstone.laperinapp.data.response.DataRecipes
import com.capstone.laperinapp.data.response.DataUser
import com.capstone.laperinapp.data.response.ClosestDonationsResponses
import com.capstone.laperinapp.data.response.DataIngredient
import com.capstone.laperinapp.data.response.DetailRecipesResponses
import com.capstone.laperinapp.data.response.DetailUserResponse
import com.capstone.laperinapp.data.response.DonationsResponses
import com.capstone.laperinapp.data.response.EditProfileResponse
import com.capstone.laperinapp.data.response.IngredientItem
import com.capstone.laperinapp.data.response.IngredientsItem
import com.capstone.laperinapp.data.response.IngredientsResponse
import com.capstone.laperinapp.data.response.LoginResponse
import com.capstone.laperinapp.data.response.RecipeResponses
import com.capstone.laperinapp.data.response.RegisterResponse
import com.capstone.laperinapp.data.response.SearchIngredientResultResponse
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
  
    @GET("donations/closest")
    suspend fun getAllClosestDonation(
        @Query("lon") lon: Double,
        @Query("lat") lat: Double,
        @Query("page") page: Int,
        @Query("pageSize") size: Int
    ): ClosestDonationsResponses

    @GET("donations")
    suspend fun getAllDonation(
        @Query("page") page: Int,
        @Query("pageSize") size: Int
    ): DonationsResponses

    @GET("ingredients")
    suspend fun getIngredient(
        @Query("page") page: Int,
        @Query("pageSize") size: Int
    ): IngredientsResponse

    @GET("ingredients/search/name")
    suspend fun getIngredientsByName(
        @Query("q") name: String,
        @Query("page") page: Int,
        @Query("pageSize") size: Int
    ): SearchIngredientResultResponse

    @Multipart
    @POST("donations")
    suspend fun addDonation(
        @Part("idUser") id: RequestBody,
        @Part("username") username: RequestBody,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("category") category: RequestBody,
        @Part("total") total: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("lat") latitude: RequestBody,
        @Part("lon") longitude: RequestBody
    ): Response<AddDonationResponse>

    @Multipart
    @POST("donations")
    fun addsDonation(
        @Part("idUser") id: RequestBody,
        @Part("username") username: RequestBody,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("category") category: RequestBody,
        @Part("total") total: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("lat") latitude: RequestBody,
        @Part("lon") longitude: RequestBody
    ): Call<AddDonationResponse>
}