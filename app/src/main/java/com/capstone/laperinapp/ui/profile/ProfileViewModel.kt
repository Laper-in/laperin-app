package com.capstone.laperinapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.capstone.laperinapp.data.Repository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ProfileViewModel (private val repository: Repository): ViewModel() {

    fun getUser() = repository.getDetailUser()

    fun getAllBookmark(category:String) = repository.getAllBookmarks(category).cachedIn(viewModelScope)

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
    fun logoutUser() {
        viewModelScope.launch {
            repository.logoutUser()
        }
    }
    suspend fun getSession() {
        repository.getSession().first()
    }
    fun updateImageUser( image: MultipartBody.Part) =
        repository.updateImageUser(image)
}