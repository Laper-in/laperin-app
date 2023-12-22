package com.capstone.laperinapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.liveData
import com.capstone.laperinapp.data.room.favorite.dao.FavoriteDao
import com.capstone.laperinapp.data.room.favorite.entity.Favorite
import com.capstone.laperinapp.data.paging.BookmarksPagingSource
import com.capstone.laperinapp.data.paging.ClosestDonationsPagingSource
import com.capstone.laperinapp.data.paging.MyCompletedDonationPagingSource
import com.capstone.laperinapp.data.paging.MyUncompletedDonationPagingSource
import com.capstone.laperinapp.data.paging.RecipesPagingSource
import com.capstone.laperinapp.data.paging.RecipesRecomPagingSource
import com.capstone.laperinapp.data.paging.RecommendationPagingSource
import com.capstone.laperinapp.data.paging.SearchIngredientPagingSource
import com.capstone.laperinapp.data.paging.SearchRecipesByCategoryPagingSource
import com.capstone.laperinapp.data.paging.SearchRecipesPagingSource
import com.capstone.laperinapp.data.pref.UserModel
import com.capstone.laperinapp.data.pref.UserPreference
import com.capstone.laperinapp.data.response.DataItemBookmark
import com.capstone.laperinapp.data.response.ErrorResponse
import com.capstone.laperinapp.data.response.DataItemDonation
import com.capstone.laperinapp.data.response.DataItemIngredient
import com.capstone.laperinapp.data.response.DataItemRecipes
import com.capstone.laperinapp.helper.Result
import com.capstone.laperinapp.data.retrofit.ApiService
import com.capstone.laperinapp.data.retrofit.ingredient.ApiServiceIngredient
import com.capstone.laperinapp.data.room.result.dao.ResultDao
import com.capstone.laperinapp.data.room.result.entity.ScanResult
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.math.BigInteger


class Repository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val scanDao: ResultDao,
    private val favoriteDao: FavoriteDao,
    private val apiServiceIngredient: ApiServiceIngredient
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
    fun setLogin(username : String, password: String) = liveData{
        emit(Result.Loading)
        try {
            val response = apiService.login(username, password)
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

    fun getAllRecipes(): LiveData<PagingData<DataItemRecipes>> {
        return Pager(
            config = PagingConfig(
                pageSize = 50
            ),
            pagingSourceFactory = {
                RecipesPagingSource(apiService)
            }
        ).liveData
    }

    fun getAllRecipesRandom(): LiveData<PagingData<DataItemRecipes>> {
        return Pager(
            config = PagingConfig(
                pageSize = 50
            ),
            pagingSourceFactory = {
                RecipesRecomPagingSource(apiService)
            }
        ).liveData
    }

    fun getRecipesByName(name: String): LiveData<PagingData<DataItemRecipes>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                SearchRecipesPagingSource(apiService, name)
            }
        ).liveData
    }

    fun getRecipesByCategory(name: String): LiveData<PagingData<DataItemRecipes>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                SearchRecipesByCategoryPagingSource(apiService, name)
            }
        ).liveData
    }

    fun getRecommendation(name: String): LiveData<PagingData<DataItemRecipes>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                RecommendationPagingSource(apiService, name)
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

       fun getDetailUser() = liveData {
         emit(Result.Loading)
         try {
             val response = apiService.getDetailUser()
             if (response.isSuccessful) {
                 emit(Result.Success(response.body()!!))
                 Log.d(TAG, "getDetailUser: ${response.body()}")
             } else {
                 val errorResponse = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                 Log.e(TAG, "getDetailUsers: $response", )
                 emit(Result.Error(errorResponse.message.toString()))
             }
         }catch (e:Exception) {
             emit(Result.Error(e.message.toString()))
             Log.e(TAG, "getDetailUser: ${e.message}", )
         }
     }

    fun logoutUser() = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.logout()
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
                Log.d(TAG, "logoutUser: ${response.body()}")
            } else {
                val errorResponse = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                Log.e(TAG, "logoutUser: $response", )
                emit(Result.Error(errorResponse.message.toString()))
            }
        }catch (e:Exception) {
            emit(Result.Error(e.message.toString()))
            Log.e(TAG, "logoutUser: ${e.message}", )
        }
    }

    fun updateUser(email: String, fullname: String, alamat: String, telephone: BigInteger) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.updateDetailUser(email, fullname, alamat, telephone)
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

    fun updateImageUser( image: MultipartBody.Part) = liveData{
        emit(Result.Loading)
        try {
            val response = apiService.updateImageUser(image)
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

    fun getAllBookmarks(category: String): LiveData<PagingData<DataItemBookmark>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                BookmarksPagingSource(apiService, category)
            }
        ).liveData
    }

    fun getClosestDonation(longitude: Float, latitude: Float): LiveData<PagingData<DataItemDonation>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                ClosestDonationsPagingSource(apiService, longitude, latitude)
            }
        ).liveData
    }

    fun getDetailDonation(id: String) = liveData{
        emit(Result.Loading)
        try {
            val response = apiService.getDetailDonation(id)
            if (response.isSuccessful) {
                emit(Result.Success(response.body()?.data!!))
                Log.d(TAG, "getDetailDonation: ${response.body()}")
            } else {
                val errorResponse = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                Log.e(TAG, "getDetailDonation: $response", )
                emit(Result.Error(errorResponse.message.toString()))
            }
        }catch (e:Exception) {
            emit(Result.Error(e.message.toString()))
            Log.e(TAG, "getDetailDonation: ${e.message}", )
        }
    }

    fun getMyUncompletedDonations(): LiveData<PagingData<DataItemDonation>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                MyUncompletedDonationPagingSource(apiService)
            }
        ).liveData
    }

    fun getMyCompletedDonations(): LiveData<PagingData<DataItemDonation>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                MyCompletedDonationPagingSource(apiService)
            }
        ).liveData
    }

    fun deleteDonation(id: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.deleteDonation(id)
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
                Log.d(TAG, "deleteDonation: ${response.body()}")
            } else {
                val errorResponse = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                emit(Result.Error(errorResponse.message.toString()))
                Log.e(TAG, "deleteDonation: ${errorResponse.message}", )
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
            Log.e(TAG, "deleteDonation: $e", )
        }
    }

    fun getIngredientsByName(name: String): LiveData<PagingData<DataItemIngredient>> {
        return Pager(
            config = PagingConfig(
                pageSize = 50
            ),
            pagingSourceFactory = {
                SearchIngredientPagingSource(apiService, name)
            }
        ).liveData
    }

    fun createDonation(
        username: RequestBody,
        name: RequestBody,
        description: RequestBody,
        category: RequestBody,
        total: RequestBody,
        longitude: RequestBody,
        latitude: RequestBody,
        image: MultipartBody.Part,
        userImage: RequestBody,
        telephone: RequestBody
    ) = liveData{
        emit(Result.Loading)
        try {
            val response = apiService.addDonation(username, name, description, category, total, image, latitude, longitude, userImage, telephone )
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

    fun getAllBookmark() = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllBookmarks()
            if (response.isSuccessful) {
                emit(Result.Success(response.body()?.data!!))
                Log.d(TAG, "getAllBookmark: ${response.body()}")
            } else {
                val errorResponse =
                    Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                Log.e(TAG, "getAllBookmark: $response",)
                emit(Result.Error(errorResponse.message.toString()))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
            Log.e(TAG, "getAllBookmark: ${e.message}",)
        }
    }

    fun insertBookmark(idRecipe: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.addBookmark(idRecipe)
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
                Log.d(TAG, "insertBookmark: ${response.body()}")
            } else {
                val errorResponse =
                    Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                Log.e(TAG, "insertBookmark: $response",)
                emit(Result.Error(errorResponse.message.toString()))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
            Log.e(TAG, "insertBookmark: ${e.message}",)
        }
    }

    fun deleteBookmark(idRecipe: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.deleteBookmark(idRecipe)
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
                Log.d(TAG, "deleteBookmark: ${response.body()}")
            } else {
                val errorResponse =
                    Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                Log.e(TAG, "deleteBookmark: $response",)
                emit(Result.Error(errorResponse.message.toString()))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
            Log.e(TAG, "deleteBookmark: ${e.message}",)
        }
    }

    fun getCategoryByBookmark() = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getCategory()
            if (response.isSuccessful) {
                emit(Result.Success(response.body()?.data!!))
                Log.d(TAG, "getCategoryByBookmark: ${response.body()}")
            } else {
                val errorResponse =
                    Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                Log.e(TAG, "getCategoryByBookmark: $response",)
                emit(Result.Error(errorResponse.message.toString()))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
            Log.e(TAG, "getCategoryByBookmark: ${e.message}",)
        }
    }

    fun getAllCategory() = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllCategory()
            if (response.isSuccessful) {
                emit(Result.Success(response.body()?.data!!))
                Log.d(TAG, "getAllCategory: ${response.body()}")
            } else {
                val errorResponse =
                    Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                Log.e(TAG, "getAllCategorys: $response",)
                emit(Result.Error(errorResponse.message.toString()))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
            Log.e(TAG, "getAllCategoryss: ${e.message}",)
        }
    }

    fun searchResultScan(name: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiServiceIngredient.searchScan(name)
            if (response.isSuccessful){
                emit(Result.Success(response.body()!!))
                Log.d(TAG, "searchResultScan: $response")
            } else {
                val errorResponse = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                emit(Result.Error(errorResponse.message.toString()))
                Log.e(TAG, "searchResultScan: $response", )
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
            Log.e(TAG, "searchResultScans: ${e.message}", )
        }
    }

    fun insertResult(data: ScanResult) {
        scanDao.insert(data)
    }

    fun getAllResult(): LiveData<List<ScanResult>> {
        return scanDao.getAllResult()
    }

    fun getAllResultContainsName(name: String): Int {
        return scanDao.getAllResultContainsName(name)
    }

    fun getAllResultName(): LiveData<List<String>> {
        return scanDao.getAllResultName()
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
            favoriteDao: FavoriteDao,
            apiServiceIngredient: ApiServiceIngredient
        ): Repository =
            instance ?: synchronized(this){
                instance ?: Repository(apiService, userPreference, scanDao, favoriteDao, apiServiceIngredient)
            }.also { instance = it }

        fun clearInstance(){
            instance = null
        }
    }
}