package com.capstone.laperinapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.laperinapp.data.Repository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileViewModel (private val repository: Repository): ViewModel() {

    fun getUser(id:String) = repository.getDetailUser(id)

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
    suspend fun getSession() {
        repository.getSession().first()
    }
}