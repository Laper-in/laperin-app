package com.capstone.laperinapp.ui.history

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.laperinapp.data.favorite.room.FavoriteRoom

class HistoryViewModel(private val favoriteRoom :FavoriteRoom) : ViewModel() {
    fun  getRecipesFavorite() = favoriteRoom.favoriteDao().loadAll()
    class Factory(private  val db:FavoriteRoom) : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel> create(modelClass: Class<T>): T = HistoryViewModel(db) as T
    }
}