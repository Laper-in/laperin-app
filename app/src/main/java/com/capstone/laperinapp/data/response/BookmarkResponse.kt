package com.capstone.laperinapp.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class BookmarkResponse(

	@field:SerializedName("data")
	val data: List<DataItemBookmark>,

	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("total_pages")
	val totalPages: Int,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("current_page")
	val currentPage: Int
) : Parcelable

@Parcelize
data class Bookmark(

	@field:SerializedName("idUser")
	val idUser: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("idRecipe")
	val idRecipe: String,

	@field:SerializedName("idBookmark")
	val idBookmark: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
) : Parcelable

@Parcelize
data class DataItemBookmark(

	@field:SerializedName("bookmark")
	val bookmark: Bookmark,

	@field:SerializedName("recipe")
	val recipe: Recipe
) : Parcelable

@Parcelize
data class Recipe(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("ingredient")
	val ingredient: String,

	@field:SerializedName("updatedBy")
	val updatedBy: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("video")
	val video: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("createBy")
	val createBy: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("time")
	val time: String,

	@field:SerializedName("category")
	val category: String,

	@field:SerializedName("guide")
	val guide: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
) : Parcelable
