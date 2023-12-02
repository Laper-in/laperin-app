package com.capstone.laperinapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.laperinapp.data.Repository

class HomeViewModel(private val repository: Repository) : ViewModel() {

    fun getAllRecipes() = repository.getAllRecipes()
}