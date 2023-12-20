package com.capstone.laperinapp.ui.home.all_recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.data.response.DataItemRecipes

class AllRecipesViewModel(private val repository: Repository): ViewModel() {

    val searchLiveData = MutableLiveData<String>().apply { value = "" }

    private val resultSearchLiveData: LiveData<PagingData<DataItemRecipes>> = searchLiveData.switchMap { query ->
        if (query == "All") {
            repository.getRecipesByCategory("").cachedIn(viewModelScope)
        } else {
            repository.getRecipesByCategory(query).cachedIn(viewModelScope)
        }
    }

    fun getAllRecipesByCategory() = resultSearchLiveData
}