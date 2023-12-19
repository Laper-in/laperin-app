package com.capstone.laperinapp.ui.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.data.pref.UserModel
import kotlinx.coroutines.launch

class WelcomeViewModel(private val repository: Repository) : ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}