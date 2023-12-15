package com.capstone.laperinapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.capstone.laperinapp.data.paging.ClosestDonationsPagingSource
import com.capstone.laperinapp.data.paging.DonationsPagingSource
import com.capstone.laperinapp.data.paging.RecipesPagingSource
import com.capstone.laperinapp.data.pref.UserModel
import com.capstone.laperinapp.data.pref.UserPreference
import com.capstone.laperinapp.data.response.AddDonationResponse
import com.capstone.laperinapp.data.response.DetailUserResponse
import com.capstone.laperinapp.data.response.ErrorResponse
import com.capstone.laperinapp.data.response.ClosestDonationsItem
import com.capstone.laperinapp.data.response.DataAddDonation
import com.capstone.laperinapp.data.response.DonationsItem
import com.capstone.laperinapp.data.response.RecipeItem
import com.capstone.laperinapp.helper.Result
import com.capstone.laperinapp.data.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Repository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun registerUser(
        username: String,
        email: String,
        password: String

    ) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(username, email, password)
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
            } else {
                val errorResponse =
                    Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                emit(Result.Error(errorResponse.message.toString()))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun setLogin(email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
            } else {
                val errorResponse =
                    Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                emit(Result.Error(errorResponse.message.toString()))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getAllRecipes(): LiveData<PagingData<RecipeItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                RecipesPagingSource(apiService)
            }
        ).liveData
    }

    fun getDetailRecipes(id: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailRecipes(id)
            if (response.isSuccessful) {
                emit(Result.Success(response.body()?.data!!))
            } else {
                val errorResponse =
                    Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                emit(Result.Error(errorResponse.message.toString()))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getDetailUser(id: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailUser(id)
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
            } else {
                val errorResponse =
                    Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                emit(Result.Error(errorResponse.message.toString()))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun editProfile(id: String, fullname: String, picture: String, alamat: String, telephone: Int) =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.updateDetailUser(id, fullname, picture, alamat, telephone)
                if (response.isSuccessful) {
                    emit(Result.Success(response.body()!!))
                } else {
                    val errorResponse =
                        Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                    emit(Result.Error(errorResponse.message.toString()))
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun getClosestDonation(
        longitude: Double,
        latitude: Double
    ): LiveData<PagingData<ClosestDonationsItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                ClosestDonationsPagingSource(apiService, longitude, latitude)
            }
        ).liveData
    }

    fun getAllDonations(): LiveData<PagingData<DonationsItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                DonationsPagingSource(apiService)
            }
        ).liveData
    }

    suspend fun createDonation(
        userId: RequestBody,
        username: RequestBody,
        name: RequestBody,
        description: RequestBody,
        category: RequestBody,
        total: RequestBody,
        longitude: RequestBody,
        latitude: RequestBody,
        image: MultipartBody.Part
    ) : AddDonationResponse {
        return try {
            val response = apiService.addDonation(
                userId,
                username,
                name,
                description,
                category,
                total,
                image,
                latitude,
                longitude
            )
            Log.d(TAG, "createDonation: ${response.message}")
            response
        } catch (e: Exception) {
            Log.e(TAG, "createDonation: ${e.message}", )
            throw Exception(e.message.toString())
        }
    }

    fun addsDonations(
        userId: RequestBody,
        username: RequestBody,
        name: RequestBody,
        description: RequestBody,
        category: RequestBody,
        total: RequestBody,
        longitude: RequestBody,
        latitude: RequestBody,
        image: MultipartBody.Part
    ) : LiveData<Result<DataAddDonation>> {
        val result = MutableLiveData<Result<DataAddDonation>>()
        result.value = Result.Loading

        val client = apiService.addsDonation(
            userId,
            username,
            name,
            description,
            category,
            total,
            image,
            latitude,
            longitude
        )
        client.enqueue(object : Callback<AddDonationResponse> {
            override fun onResponse(
                call: Call<AddDonationResponse>,
                response: Response<AddDonationResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    result.value = Result.Success(responseBody.data)
                } else {
                    result.value = Result.Error(response.message())
                    Log.e(TAG, "onResponseSuccess: ${response.message()}", )
                }
            }

            override fun onFailure(call: Call<AddDonationResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }

        })
        return result
    }

    companion object {
        private const val TAG = "Repository"

        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, userPreference)
            }.also { instance = it }

        fun clearInstance() {
            instance = null
        }
    }
}