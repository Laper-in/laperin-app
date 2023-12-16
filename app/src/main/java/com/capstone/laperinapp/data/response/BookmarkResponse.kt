package com.capstone.laperinapp.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class BookmarkResponse(

	@field:SerializedName("bookmarks")
	val bookmarks: List<BookmarksItem>,

	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("total_pages")
	val totalPages: Int,

	@field:SerializedName("current_page")
	val currentPage: Int
) : Parcelable

@Parcelize
data class BookmarksItem(

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
