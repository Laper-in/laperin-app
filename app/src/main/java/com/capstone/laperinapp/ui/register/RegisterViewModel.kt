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
    private val _registrationResult = MutableLiveData<Result<RegisterResponse>>()
    val registrationResult: LiveData<Result<RegisterResponse>> get() = _registrationResult
    val isLoading : LiveData<Boolean> = repository.isLoading

    fun registerUser(
        username: String,
        email: String,
        fullname: String,
        password: String,
        picture: String?,
        alamat: String,
        telephone: Int,

    ) {
        viewModelScope.launch {
            _registrationResult.value = Result.Loading
            try {
                val response = repository.registerUser(username, email, fullname, password, "", alamat, telephone)
                _registrationResult.value = Result.Success(response)
            } catch (e: Exception) {
                _registrationResult.value = Result.Error(e.message ?: "Registration failed")
            }
        }
    }

}