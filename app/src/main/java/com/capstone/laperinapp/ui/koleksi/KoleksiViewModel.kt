package com.capstone.laperinapp.ui.koleksi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.data.response.DataItemBookmark
import com.capstone.laperinapp.data.response.DataItemIngredient

class KoleksiViewModel(private val repository :Repository) : ViewModel() {

    val searchStringLiveData = MutableLiveData<String>()

    private val searchResults: LiveData<PagingData<DataItemBookmark>> = searchStringLiveData.switchMap { query ->
        if (query == "All") {
            repository.getAllBookmarks("").cachedIn(viewModelScope)
        }else if (query == ""){
            repository.getAllBookmarks("").cachedIn(viewModelScope)
        } else {
            repository.getAllBookmarks(query).cachedIn(viewModelScope)
        }
    }

    fun getAllBookmarks() = searchResults

    fun getCategory() = repository.getCategoryByBookmark()

}