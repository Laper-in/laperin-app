package com.capstone.laperinapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.data.pref.UserPreference
import com.capstone.laperinapp.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class HomeViewModel(private val repository: Repository) : ViewModel() {

    fun getAllRecipes() = repository.getAllRecipes().cachedIn(viewModelScope)

    fun getAllRecipesRandom() = repository.getAllRecipesRandom().cachedIn(viewModelScope)

    fun getUser() = repository.getDetailUser()

    suspend fun getSession() {
        repository.getSession().first()
    }
}