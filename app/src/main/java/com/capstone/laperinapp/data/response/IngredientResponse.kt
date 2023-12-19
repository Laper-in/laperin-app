package com.capstone.laperinapp.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class IngredientResponse(

	@field:SerializedName("data")
	val data: List<DataItemIngredient>,

	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("total_pages")
	val totalPages: Int,

	@field:SerializedName("message")
	val message: String
) : Parcelable

@Parcelize
data class DataItemIngredient(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
) : Parcelable
