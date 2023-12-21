package com.capstone.laperinapp.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class AuthResponse(

	@field:SerializedName("data")
	val data: DataAuthUser,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("accessToken")
	val accessToken: String
) : Parcelable

@Parcelize
data class DataAuthUser(

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String
) : Parcelable

@Parcelize
data class UserDetailResponse(

	@field:SerializedName("data")
	val data: DataDetailUser,

	@field:SerializedName("message")
	val message: String
) : Parcelable

@Parcelize
data class DataDetailUser(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("updatedBy")
	val updatedBy: String,

	@field:SerializedName("telephone")
	val telephone: Long,

	@field:SerializedName("isOnline")
	val isOnline: Boolean,

	@field:SerializedName("deletedBy")
	val deletedBy: String,

	@field:SerializedName("alamat")
	val alamat: String,

	@field:SerializedName("isChef")
	val isChef: Boolean,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("deletedAt")
	val deletedAt: String,

	@field:SerializedName("isDeleted")
	val isDeleted: Boolean,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("fullname")
	val fullname: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("isPro")
	val isPro: Boolean,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
) : Parcelable

@Parcelize
data class DataEditProfile(

	@field:SerializedName("id")
	val id: String = "",

	@field:SerializedName("username")
	val username: String = "",

	@field:SerializedName("email")
	val email: String = "",

	@field:SerializedName("fullname")
	val fullname: String = "",

	@field:SerializedName("telephone")
	val telephone: Long = 0,

	@field:SerializedName("alamat")
	val alamat: String = ""
) : Parcelable

@Parcelize
data class DataUpdateImage(
	@field:SerializedName("image")
	val image: String,
): Parcelable

@Parcelize
data class UpdateResponse(

	@field:SerializedName("message")
	val message: String

) : Parcelable