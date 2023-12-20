package com.capstone.laperinapp.data.retrofit

import androidx.room.Delete
import com.capstone.laperinapp.data.response.AllBookmarkResponse
import com.capstone.laperinapp.data.response.BookmarkResponse
import com.capstone.laperinapp.data.response.ClosestDonationsResponses
import com.capstone.laperinapp.data.response.DonationsResponses
import com.capstone.laperinapp.data.response.IngredientsResponse
import com.capstone.laperinapp.data.response.RecipeResponse
import com.capstone.laperinapp.data.response.SearchIngredientResultResponse
import com.capstone.laperinapp.data.response.AuthResponse
import com.capstone.laperinapp.data.response.CreateDonationResponse
import com.capstone.laperinapp.data.response.DetailDonationResponse
import com.capstone.laperinapp.data.response.DonationResponse
import com.capstone.laperinapp.data.response.IngredientResponse
import com.capstone.laperinapp.data.response.RecipeDetailResponse
import com.capstone.laperinapp.data.response.UpdateResponse
import com.capstone.laperinapp.data.response.UserDetailResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.DELETE
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
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<AuthResponse>

    @FormUrlEncoded
    @POST("users/signup")
    suspend fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<AuthResponse>

    @GET("users/id")
    suspend fun getDetailUser(
    ): Response<UserDetailResponse>

    @FormUrlEncoded
    @PATCH("users/id")
    suspend fun updateDetailUser(
        @Field("email") email: String,
        @Field("fullname") fullname: String,
        @Field("alamat") alamat: String,
        @Field("telephone") telephone: BigInteger
    ):  Response<UpdateResponse>

    @Multipart
    @PATCH("users/id")
    suspend fun updateImageUser(
        @Part image: MultipartBody.Part,
    ):  Response<UpdateResponse>

    @POST("users/signout")
    suspend fun logout(
    ): Response<UpdateResponse>

    @GET("recipes")
    suspend fun getAllRecipes(
        @Query("page") page: Int,
        @Query("pageSize") size: Int
    ): RecipeResponse

    @GET("recipes/search/name")
    suspend fun getRecipesByName(
        @Query("q") name: String,
        @Query("page") page: Int,
        @Query("pageSize") size: Int
    ): RecipeResponse

    @GET("recipes/search/category")
    suspend fun getRecipesByCategory(
        @Query("q") name: String,
        @Query("page") page: Int,
        @Query("pageSize") size: Int
    ): RecipeResponse

    @GET("recipes/search/ingredient")
    suspend fun getRecommendation(
        @Query("q") name: String,
        @Query("page") page: Int,
        @Query("pageSize") size: Int
    ): RecipeResponse

    @GET("recipes/{id}")
    suspend fun getDetailRecipes(
        @Path("id") id: String
    ): Response<RecipeDetailResponse>

    @GET("bookmarks/search")
    suspend fun getBookmarks(
        @Query("category") category: String,
        @Query("page") page: Int,
        @Query("pageSize") size: Int
    ): BookmarkResponse
  
    @GET("donations/closest/{lon}/{lat}")
    suspend fun getAllClosestDonation(
        @Path("lon") lon: Float,
        @Path("lat") lat: Float,
        @Query("page") page: Int,
        @Query("pageSize") size: Int
    ): DonationResponse

    @GET("ingredients/search")
    suspend fun getIngredientsByName(
        @Query("q") name: String,
        @Query("page") page: Int,
        @Query("pageSize") size: Int
    ): IngredientResponse

    @Multipart
    @POST("donations")
    suspend fun addDonation(
        @Part("username") username: RequestBody,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("category") category: RequestBody,
        @Part("total") total: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("lat") latitude: RequestBody,
        @Part("lon") longitude: RequestBody
    ): Response<DonationResponse>

    @GET("donations/user")
    suspend fun getDonationByUser(
        @Query("page") page: Int,
        @Query("pageSize") size: Int
    ): DonationResponse

    @DELETE("donations/{id}")
    suspend fun deleteDonation(
        @Path("id") id: String
    ): Response<CreateDonationResponse>

    @GET("donations/{id}")
    suspend fun getDetailDonation(
        @Path("id") id: String
    ): Response<DetailDonationResponse>

    @GET("bookmarks")
    suspend fun getAllBookmarks(
    ): Response<AllBookmarkResponse>

    @FormUrlEncoded
    @POST("bookmarks")
    suspend fun addBookmark(
        @Field("idRecipe") idRecipe: String
    ): Response<CreateDonationResponse>

    @DELETE("bookmarks/{id}")
    suspend fun deleteBookmark(
        @Path("id") id: String
    ): Response<CreateDonationResponse>
}