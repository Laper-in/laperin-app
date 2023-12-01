package com.capstone.laperinapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.capstone.laperinapp.data.pref.UserModel
import com.capstone.laperinapp.data.pref.UserPreference
import com.capstone.laperinapp.data.response.ErrorResponse
import com.capstone.laperinapp.data.response.LoginResponse
import com.capstone.laperinapp.data.response.RegisterResponse
import com.capstone.laperinapp.data.retrofit.ApiConfig
import com.capstone.laperinapp.helper.Result
import com.capstone.laperinapp.data.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response


class Repository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
){

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    private val _successMessage = MutableLiveData<String?>()
    val successMessage: LiveData<String?>
        get() = _successMessage

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse : LiveData<LoginResponse> get() = _loginResponse
    suspend fun registerUser(
        username: String,
        email: String,
        fullname: String,
        password: String,
        picture: String,
        alamat: String,
        telephone: Int,

    ): RegisterResponse {
        try {
            val response = apiService.register(username, email, fullname, password, picture,  alamat,telephone)
            if (response.isSuccessful) {
                val userModel = UserModel(email, response.body()?.data?.picture ?: "", true)
                userPreference.saveSession(userModel)
                return response.body()!!
            } else {
                Log.e("Registration", "Error: ${response.errorBody()?.string()}")
                throw Exception("Registration failed")
            }
        } catch (e: Exception) {
            Log.e("Registration", "Error: ${e.message}")
            throw Exception("Registration failed")
        }
    }
    fun setLogin(email : String, password: String){
        _isLoading.value = false
        _isLoading.value = true
        ApiConfig.getApiService(email)
            (object : Callback<LoginResponse>{
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body() != null){
                        _loginResponse.value = response.body()
                        _isLoading.value = false
                    } else {
                        _errorMessage.value = response.message()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _isLoading.value = true
                    _errorMessage.value = t.message
                    Log.d("User Login", " onFailure : ${t.message}")
                }
            })
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