package com.capstone.laperinapp.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.capstone.laperinapp.data.response.RecipesItem
import com.capstone.laperinapp.data.retrofit.ApiService

class SearchRecipesPagingSource(private val apiService: ApiService, private val name: String) : PagingSource<Int, RecipesItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RecipesItem> {
        return try {
            val position = params.key ?: PAGE_INDEX
            val response = apiService.getRecipesByName(name, position, params.loadSize).data.recipes
            Log.d(TAG, "response: $response")

            LoadResult.Page(
                data = response,
                prevKey = if (position == PAGE_INDEX) null else position - 1,
                nextKey = if (response.isNullOrEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            Log.e(TAG, "response: $e", )
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RecipesItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val PAGE_INDEX = 1
        private const val TAG = "SearchRecipesPagingSource"
    }

}