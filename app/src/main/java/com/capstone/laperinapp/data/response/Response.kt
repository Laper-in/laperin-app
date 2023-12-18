package com.capstone.laperinapp.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class LoginResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("token")
    val token: String
) : Parcelable

@Parcelize
data class RegisterResponse(

    @field:SerializedName("data")
    val data: Data,

    @field:SerializedName("message")
    val message: String
) : Parcelable

@Parcelize
data class Data(

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("username")
    val username: String,

    ) : Parcelable

data class EditProfileResponse(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("fullname")
    val fullname: String,

    @field:SerializedName("picture")
    val picture: String,

    @field:SerializedName("alamat")
    val alamat: String,

    @field:SerializedName("telephone")
    val telephone: Int,
)

@Parcelize
data class ErrorResponse(
    @field:SerializedName("error")
    val error: Boolean? = null,
    @field:SerializedName("message")
    val message: String? = null
) : Parcelable

@Parcelize
data class RecipesResponses(

    @field:SerializedName("data")
    val data: List<DataRecipes>,

    @field:SerializedName("message")
    val message: String
) : Parcelable

@Parcelize
data class DataRecipes(

    @field:SerializedName("image")
    val image: String, //tomat, wortel, bla bla

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("ingredient")
    val ingredient: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("category")
    val category: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
) : Parcelable

@Parcelize
data class RecipessResponses(

    @field:SerializedName("total_count")
    val totalCount: Int,

    @field:SerializedName("recipe")
    val recipe: List<RecipeItem>,

    @field:SerializedName("total_pages")
    val totalPages: Int
) : Parcelable

@Parcelize
data class RecipeItem(

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("ingredient")
    val ingredient: String,

    @field:SerializedName("urlVideo")
    val urlVideo: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("category")
    val category: String,

    @field:SerializedName("guide")
    val guide: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
) : Parcelable

@Parcelize
data class DetailRecipesResponses(

    @field:SerializedName("data")
    val data: DataDetailRecipes,

    @field:SerializedName("message")
    val message: String
) : Parcelable

@Parcelize
data class DataDetailRecipes(

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("ingredient")
    val ingredient: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("category")
    val category: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String,

    @field:SerializedName("urlVideo")
    val urlVideo: String,

    @field:SerializedName("guide")
    val guide: String

) : Parcelable

@Parcelize
data class ClosestDonationsResponses(

    @field:SerializedName("total_count")
    val totalCount: Int,

    @field:SerializedName("closestDonations")
    val closestDonations: List<ClosestDonationsItem>,

    @field:SerializedName("total_pages")
    val totalPages: Int,

    @field:SerializedName("current_page")
    val currentPage: Int
) : Parcelable

@Parcelize
data class ClosestDonationsItem(

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("distance")
    val distance: Int,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("lon")
    val lon: Double,

    @field:SerializedName("idDonation")
    val idDonation: String,

    @field:SerializedName("idUser")
    val idUser: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("total")
    val total: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("category")
    val category: String,

    @field:SerializedName("lat")
    val lat: Double,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
) : Parcelable

@Parcelize
data class DonationsResponses(

    @field:SerializedName("donations")
    val donations: List<DonationsItem>,

    @field:SerializedName("total_count")
    val totalCount: Int,

    @field:SerializedName("total_pages")
    val totalPages: Int,

    @field:SerializedName("current_page")
    val currentPage: Int
) : Parcelable

@Parcelize
data class DonationsItem(

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("distance")
    val distance: Double,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("lon")
    val lon: Double,

    @field:SerializedName("idDonation")
    val idDonation: String,

    @field:SerializedName("idUser")
    val idUser: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("total")
    val total: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("category")
    val category: String,

    @field:SerializedName("lat")
    val lat: Double,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
) : Parcelable

@Parcelize
data class SearchIngredientResultResponse(

    @field:SerializedName("data")
    val data: DataIngredient,

    @field:SerializedName("message")
    val message: String
) : Parcelable

@Parcelize
data class DataIngredient(

    @field:SerializedName("total_count")
    val totalCount: Int,

    @field:SerializedName("total_pages")
    val totalPages: Int,

    @field:SerializedName("users")
    val users: List<IngredientItem>
) : Parcelable

@Parcelize
data class IngredientItem(

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
) : Parcelable

@Parcelize
data class IngredientsResponse(

    @field:SerializedName("total_count")
    val totalCount: Int,

    @field:SerializedName("recipe")
    val recipe: List<IngredientsItem>,

    @field:SerializedName("total_pages")
    val totalPages: Int
) : Parcelable

@Parcelize
data class IngredientsItem(

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
) : Parcelable
