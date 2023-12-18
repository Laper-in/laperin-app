package com.capstone.laperinapp.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DonationResponse(

    @field:SerializedName("data")
    val data: List<DataItemDonation>,

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
data class DataItemDonation(

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

    @field:SerializedName("isDone")
    val isDone: Boolean,

    @field:SerializedName("deletedBy")
    val deletedBy: String,

    @field:SerializedName("idUser")
    val idUser: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("total")
    val total: Int,

    @field:SerializedName("deletedAt")
    val deletedAt: String,

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
data class CreateDonationResponse(

    @field:SerializedName("message")
    val message: String

) : Parcelable