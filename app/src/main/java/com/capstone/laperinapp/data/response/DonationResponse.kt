package com.capstone.laperinapp.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddDonationResponse(

    @field:SerializedName("data")
    val data: DataAddDonation,

    @field:SerializedName("message")
    val message: String
) : Parcelable

@Parcelize
data class DataAddDonation(

    @field:SerializedName("idUser")
    val idUser: String,

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("total")
    val total: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("lon")
    val lon: Float,

    @field:SerializedName("category")
    val category: String,

    @field:SerializedName("idDonation")
    val idDonation: String,

    @field:SerializedName("lat")
    val lat: Float,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String

) : Parcelable