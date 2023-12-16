package com.capstone.laperinapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.capstone.laperinapp.data.Repository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileViewModel (private val repository: Repository): ViewModel() {

    fun getUser(id:String) = repository.getDetailUser(id)

    fun getAllBookmark(id:String) = repository.getAllBookmarksById(id).cachedIn(viewModelScope)

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
    suspend fun getSession() {
        repository.getSession().first()
    }
}