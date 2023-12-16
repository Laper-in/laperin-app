package com.capstone.laperinapp.ui.home.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.capstone.laperinapp.data.Repository

class SearchRecipesViewModel(private val repository: Repository): ViewModel() {

    val searchQuery = MutableLiveData<String>()

    fun getRecipesByName() = searchQuery.switchMap { query ->
        repository.getRecipesByName(query).cachedIn(viewModelScope)
    }
}