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

    private val resultInsertFavorite = MutableLiveData<Boolean>()
    private val resultDeleteFavorite = MutableLiveData<Boolean>()

    fun getRecipeById(id: String) = repository.getDetailRecipes(id)

    fun getAllFavorite() = repository.getAllFavorite()

    fun insertFavorite(favorite: Favorite) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertFavorite(favorite)
            resultInsertFavorite.postValue(true)
        }
    }


    fun deleteFavorite(favorite: Favorite) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFavorite(favorite)
            resultDeleteFavorite.postValue(true)
        }
    }


    fun findFavorite(name: String, listenFavorite: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val recipes = repository.findFavoriteById(name)
            if (recipes != null) {
                withContext(Dispatchers.Main) {
                    listenFavorite()
                }
            }
        }
    }
}