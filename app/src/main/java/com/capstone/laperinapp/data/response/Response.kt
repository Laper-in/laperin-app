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

@Parcelize
data class DetailUserResponse(

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("updatedBy")
	val updatedBy: Int,

	@field:SerializedName("telephone")
	val telephone: Int,

	@field:SerializedName("picture")
	val picture: String,

	@field:SerializedName("deletedBy")
	val deletedBy: Int,

	@field:SerializedName("alamat")
	val alamat: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("deletedAt")
	val deletedAt: String,

	@field:SerializedName("isDeleted")
	val isDeleted: Boolean,

	@field:SerializedName("id")
	val id: Int,

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
data class ErrorResponse (
	@field:SerializedName("error")
	val error: Boolean? = null,
	@field:SerializedName("message")
	val message: String? = null
) : Parcelable



