package com.capstone.laperinapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.helper.Result
import com.capstone.laperinapp.data.response.RegisterResponse
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: Repository) : ViewModel() {

    fun registerUser(
        username: String,
        email: String,
        password: String,

    ) = repository.registerUser(username, email, password)

}