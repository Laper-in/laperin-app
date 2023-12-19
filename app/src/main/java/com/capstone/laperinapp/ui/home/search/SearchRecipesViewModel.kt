package com.capstone.laperinapp.ui.home.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.data.response.DataItemRecipes

class SearchRecipesViewModel(private val repository: Repository): ViewModel() {

    val searchLiveData = MutableLiveData<String>().apply { value = "" }

    val searchResult: LiveData<PagingData<DataItemRecipes>> = searchLiveData.switchMap { query ->
        if (query == "") {
            repository.getRecipesByName("").cachedIn(viewModelScope)
        }else {
            repository.getRecipesByName(query).cachedIn(viewModelScope)
        }
    }

    fun searchByName() = searchResult
}