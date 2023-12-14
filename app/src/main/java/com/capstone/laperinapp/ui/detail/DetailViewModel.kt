package com.capstone.laperinapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.data.favorite.entity.Favorite
import com.capstone.laperinapp.data.response.DataDetailRecipes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel(
    private val repository: Repository): ViewModel() {
    private val _recipes = MutableLiveData<DataDetailRecipes>()
    val recipes : LiveData<DataDetailRecipes> = _recipes
    val resultInsertFavorite = MutableLiveData<Boolean>()
    val resultDeleteFavorite = MutableLiveData<Boolean>()

    fun getRecipeById(id: String) = repository.getDetailRecipes(id)

    private var isFavorite = false

    fun insertFavorite(favorite: Favorite) {
        viewModelScope.launch {
            repository.insertFavorite(favorite)
            resultInsertFavorite.value = true
        }
    }

    fun deleteFavorite(favorite: Favorite) {
        viewModelScope.launch {
            repository.deleteFavorite(favorite)
            resultDeleteFavorite.value = true
        }
    }

    fun setFavorite(favorite: Favorite) {
        viewModelScope.launch {
            _recipes.value?.let {
                if (isFavorite) {
                    repository.deleteFavorite(favorite)
                    resultDeleteFavorite.value = true
                } else {
                    repository.insertFavorite(favorite)
                    resultInsertFavorite.value = true
                }
            }
            isFavorite = !isFavorite
        }
    }

    fun findFavorite(name: String, listenFavorite :() -> Unit) {
        viewModelScope.launch {
            val recipes = repository.findFavoriteById(name)
            if (recipes != null) {
                listenFavorite()
            }
        }
    }
}