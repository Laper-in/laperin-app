package com.capstone.laperinapp.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.data.favorite.room.FavoriteRoomDatabase

class HistoryViewModel(private val repository :Repository) : ViewModel() {

    fun  getRecipesFavorite() = repository.getAllFavorite()

}