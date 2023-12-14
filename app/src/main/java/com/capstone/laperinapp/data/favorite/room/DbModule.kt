package com.capstone.laperinapp.data.favorite.room

import android.content.Context
import androidx.room.Room

class DbModule (private val context: Context) {
    private val db = Room.databaseBuilder(context, FavoriteRoom::class.java, "recipesfavorite.db")
        .allowMainThreadQueries()
        .build()

    val recipesDao = db.favoriteDao()
}