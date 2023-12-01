package com.capstone.laperinapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.capstone.laperinapp.data.pref.UserModel
import com.capstone.laperinapp.data.pref.UserPreference
import com.capstone.laperinapp.data.response.ErrorResponse
import com.capstone.laperinapp.data.response.RegisterResponse
import com.capstone.laperinapp.helper.Result
import com.capstone.laperinapp.data.retrofit.ApiService
import com.google.gson.Gson


class Repository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
){

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun registerUser(
        username: String,
        email: String,
        fullname: String,
        password: String,
        picture: String,
        alamat: String,
        telephone: Int,

    ) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(username, email, fullname, password, picture, alamat, telephone)
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
            } else {
                val errorResponse = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                emit(Result.Error(errorResponse.message.toString()))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
    fun setLogin(email : String, password: String) = liveData{
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
            } else {
                val errorResponse = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                emit(Result.Error(errorResponse.message.toString()))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object{
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
        ): Repository =
            instance ?: synchronized(this){
                instance ?: Repository(apiService, userPreference)
            }.also { instance = it }

        fun clearInstance(){
            instance = null
        }
    }
}