package com.capstone.laperinapp.data.room.favorite.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorites")
@Parcelize
data class Favorite(

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "image")
    var image: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "deskripsi")
    var deskripsi: String,

    @ColumnInfo(name = "category")
    var category: String,

    @ColumnInfo(name = "ingredient")
    var ingredient: String,

    @ColumnInfo(name = "guide")
    var guide: String,

    @ColumnInfo(name = "urlVideo")
    var urlVideo: String,

) : Parcelable

