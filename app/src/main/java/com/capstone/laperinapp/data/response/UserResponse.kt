package com.capstone.laperinapp.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.math.BigInteger

@Parcelize
data class DetailUserResponse(

	@field:SerializedName("data")
	val data: DataUser,

	@field:SerializedName("message")
	val message: String
) : Parcelable

@Parcelize
data class DataUser(

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("updatedBy")
	val updatedBy: Int,

	@field:SerializedName("telephone")
	val telephone: BigInteger,

	@field:SerializedName("picture")
	val picture: String,

	@field:SerializedName("deletedBy")
	val deletedBy: String,

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
	val id: String,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("fullname")
	val fullname: String,

	@field:SerializedName("telephone")
	val telephone: BigInteger,

	@field:SerializedName("alamat")
	val alamat: String,

) : Parcelable
