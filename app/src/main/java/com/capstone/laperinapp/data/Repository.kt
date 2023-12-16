package com.capstone.laperinapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.capstone.laperinapp.data.favorite.dao.FavoriteDao
import com.capstone.laperinapp.data.favorite.entity.Favorite
import com.capstone.laperinapp.data.paging.BookmarksPagingSource
import com.capstone.laperinapp.data.paging.ClosestDonationsPagingSource
import com.capstone.laperinapp.data.paging.DonationsPagingSource
import com.capstone.laperinapp.data.paging.RecipesPagingSource
import com.capstone.laperinapp.data.paging.SearchIngredientPagingSource
import com.capstone.laperinapp.data.pref.UserModel
import com.capstone.laperinapp.data.pref.UserPreference
import com.capstone.laperinapp.data.response.BookmarksItem
import com.capstone.laperinapp.data.response.DetailUserResponse
import com.capstone.laperinapp.data.response.ErrorResponse
import com.capstone.laperinapp.data.response.ClosestDonationsItem
import com.capstone.laperinapp.data.response.DonationsItem
import com.capstone.laperinapp.data.response.IngredientItem
import com.capstone.laperinapp.data.response.RecipeItem
import com.capstone.laperinapp.helper.Result
import com.capstone.laperinapp.data.retrofit.ApiService
import com.capstone.laperinapp.data.room.result.dao.ResultDao
import com.capstone.laperinapp.data.room.result.entity.ScanResult
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import java.math.BigInteger


class Repository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val scanDao: ResultDao,
    private val favoriteDao: FavoriteDao
){

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

    fun getDetailRecipes(id: String) = liveData{
        emit(Result.Loading)
        try {
            val response = apiService.getDetailRecipes(id)
            if (response.isSuccessful) {
                emit(Result.Success(response.body()?.data!!))
            } else {
                val errorResponse = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                emit(Result.Error(errorResponse.message.toString()))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

       fun getDetailUser(id :String) = liveData {
         emit(Result.Loading)
         try {
             val response =apiService.getDetailUser(id)
             if (response.isSuccessful) {
                 emit(Result.Success(response.body()!!))
             } else {
                 val errorResponse = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                 emit(Result.Error(errorResponse.message.toString()))
             }
         }catch (e:Exception) {
             emit(Result.Error(e.message.toString()))
         }
     }

    fun editProfile(id:String, fullname: String,picture :String, alamat :String, telephone :BigInteger ) = liveData {
        emit(Result.Loading)
        try {
            val response =apiService.updateDetailUser(id, fullname, picture, alamat, telephone)
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
            } else {
                val errorResponse = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                emit(Result.Error(errorResponse.message.toString()))
            }
        }catch (e:Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun updateUser(id: String, email: String, fullname: String, alamat: String, telephone: BigInteger) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.updateDetailUser(id, email, fullname, alamat, telephone)
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

    fun getAllBookmarksById(id: String): LiveData<PagingData<BookmarksItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                BookmarksPagingSource(apiService, id)
            }
        ).liveData
    }

    fun getClosestDonation(longitude: Double, latitude: Double): LiveData<PagingData<ClosestDonationsItem>> {
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

    fun getIngredientsByName(name: String): LiveData<PagingData<IngredientItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                SearchIngredientPagingSource(apiService, name)
            }
        ).liveData
    }

    fun insertResult(data: ScanResult) {
        scanDao.insert(data)
    }

    fun getAllResult(): LiveData<List<ScanResult>> {
        return scanDao.getAllResult()
    }

    fun deleteAllResult() {
        scanDao.deleteAllResult()
    }

    fun deleteResultById(id: ScanResult) {
        scanDao.deleteById(id)
    }

    fun getAllFavorite(): LiveData<List<Favorite>> {
        return favoriteDao.getAllFavorite()
    }

    fun insertFavorite(favorite: Favorite) {
        favoriteDao.insert(favorite)
    }

    fun deleteFavorite(favorite: Favorite) {
        favoriteDao.delete(favorite)
    }

    fun findFavoriteById(id: String): Favorite? {
        return favoriteDao.findById(id)
    }

    companion object{
        private const val TAG = "Repository"

        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
            scanDao: ResultDao,
            favoriteDao: FavoriteDao
        ): Repository =
            instance ?: synchronized(this){
                instance ?: Repository(apiService, userPreference, scanDao, favoriteDao)
            }.also { instance = it }

        fun clearInstance(){
            instance = null
        }
    }
}