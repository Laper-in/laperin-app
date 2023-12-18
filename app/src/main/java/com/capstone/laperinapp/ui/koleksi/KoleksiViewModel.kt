package com.capstone.laperinapp.ui.koleksi

import androidx.lifecycle.ViewModel
import com.capstone.laperinapp.data.Repository

class KoleksiViewModel(private val repository :Repository) : ViewModel() {

    fun  getRecipesFavorite() = repository.getAllFavorite()

}