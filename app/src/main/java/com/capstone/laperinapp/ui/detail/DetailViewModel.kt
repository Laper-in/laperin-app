package com.capstone.laperinapp.ui.detail

import androidx.lifecycle.ViewModel
import com.capstone.laperinapp.data.Repository

class DetailViewModel(private val repository: Repository): ViewModel() {

    fun getRecipeById(id: String) = repository.getDetailRecipes(id)
}