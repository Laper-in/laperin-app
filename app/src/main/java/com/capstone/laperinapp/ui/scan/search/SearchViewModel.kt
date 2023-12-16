package com.capstone.laperinapp.ui.scan.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.data.paging.SearchIngredientPagingSource
import com.capstone.laperinapp.data.response.IngredientItem
import com.capstone.laperinapp.data.room.result.entity.ScanResult

class SearchViewModel(val repository: Repository): ViewModel() {

    private var currentQueryValue: String = "a"
    val searchStringLiveData = MutableLiveData<String>()

    private val searchResults: LiveData<PagingData<IngredientItem>> = searchStringLiveData.switchMap { query ->
        if (query.isNullOrEmpty() || query.isBlank()) {
            repository.getIngredientsByName("a").cachedIn(viewModelScope)
        }else {
            repository.getIngredientsByName(query).cachedIn(viewModelScope)
        }
    }

    fun getAllIngredients() = repository.getAllIngredients()

    fun insertIngredient(ingredient: ScanResult) = repository.insertResult(ingredient)

    fun getIngredientByName(): LiveData<PagingData<IngredientItem>> = searchResults

    fun submitQuery(query: String) {
        currentQueryValue = query
        searchStringLiveData.value = query
    }
}