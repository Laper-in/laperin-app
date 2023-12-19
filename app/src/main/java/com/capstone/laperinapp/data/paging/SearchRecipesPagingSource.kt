package com.capstone.laperinapp.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.capstone.laperinapp.data.response.DataItemIngredient
import com.capstone.laperinapp.data.response.DataItemRecipes
import com.capstone.laperinapp.data.retrofit.ApiService

class SearchRecipesPagingSource(private val apiService: ApiService, val name: String): PagingSource<Int, DataItemRecipes>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataItemRecipes> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getRecipesByName(name, position, params.loadSize).data

            Log.d(TAG, "response: $response")

            LoadResult.Page(
                data = response,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (response.isNullOrEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            Log.e(TAG, "response: $e")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, DataItemRecipes>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
        const val TAG = "SearchIngredientPagingSource"
    }
}