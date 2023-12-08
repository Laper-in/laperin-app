package com.capstone.laperinapp.ui.profile

import androidx.lifecycle.ViewModel
import com.capstone.laperinapp.data.Repository
import kotlinx.coroutines.flow.first

class ProfileViewModel (private val repository: Repository): ViewModel() {

    fun getUser(id:String) = repository.getDetailUser(id)

    suspend fun getSession() {
        repository.getSession().first()
    }
}