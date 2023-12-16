package com.capstone.laperinapp.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class SearchRecipesResponse(

	@field:SerializedName("data")
	val data: DataRecipe,

	@field:SerializedName("message")
	val message: String
) : Parcelable

@Parcelize
data class RecipesItem(

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
data class DataRecipe(

	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("total_pages")
	val totalPages: Int,

	@field:SerializedName("users")
	val recipes: List<RecipesItem>
) : Parcelable
